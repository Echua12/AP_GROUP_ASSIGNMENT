package com.campus.client.rag;

import com.campus.client.llm.LlmClient;
import com.campus.client.mcp.CampusMcpClient;

import java.util.Map;

/**
 * Implements Retrieval-Augmented Generation (RAG) by combining the MCP client and the LLM client:
 *
 * <ol>
 *   <li><b>Retrieve</b> &mdash; call the server's {@code search_campus_info} tool to fetch the most
 *       relevant knowledge-base passages for the question.</li>
 *   <li><b>Augment</b> &mdash; fetch the server's {@code campus_assistant} prompt as the system
 *       framing, and build a user prompt that embeds the retrieved passages as context.</li>
 *   <li><b>Generate</b> &mdash; ask the LLM to answer using only that context.</li>
 * </ol>
 *
 * Retrieval and prompt wording both come from the MCP server, so the client stays thin and the
 * "knowledge" lives in one place.
 */
public final class RagService {

    /** Bundles the answer with the context used, so the UI can show how the answer was grounded. */
    public record RagResult(String retrievedContext, String systemPrompt, String answer) {
    }

    private final CampusMcpClient mcp;
    private final LlmClient llm;

    public RagService(CampusMcpClient mcp, LlmClient llm) {
        this.mcp = mcp;
        this.llm = llm;
    }
    
    
    /**
     *  the "ask" method is the main interface in which you call the "Gemini AI" in layman terms
     *  
     *  In more specific details, it uses the "llm"'s object's "complete" method to get a String "answer" object.
     *  
     *  Before calling the "complete" method, the user's query ("question" param) must first be tokenised and matched with the existing knowledge base to return which excerpts are most relevant
     *  to the user's topic, speaking of topic, the "ask" method also takes a "topic" parameter to to "augment" the system prompt...
     * 
     *  Then, the userPrompt (the context from kb and the question itself), and the systemPrompt (gotten from campus_assistant) are fed into the llm.complete method
     *  Finally, the answer, the context (the relevant excerpts from the kb) and the question are given to a record to be retrieved later as RagResults\
     * 
     * 
     **/
    public RagResult ask(String question, String topic) throws Exception { //question is the query that the user inputs to the LLM directly, and topic is a context adder (completely optional)
        // 1. RETRIEVE: pull grounding passages from the knowledge base via the MCP tool.
        String context = mcp.callTool("search_campus_info", //DONT TOUCH SEARCH_CAMPUS_INFO, IT IS BEYOND YOU
                Map.of("query", question, "topK", 3)); //"query" is mostly for the question itself, and "topK" is the number of "top results"

        // 2. AUGMENT: use the server-provided prompt template as the system instruction.
        String systemPrompt = mcp.getPrompt("campus_assistant",
                Map.of("topic", topic == null || topic.isBlank() ? "general campus services" : topic)); //grounding prompt from the server's prompt (another long sequence of methods)

        //userPrompt is the "relevant excerpts" from "search_campus_info" combined with the user's main question
        String userPrompt = """
            Context passages from the campus knowledge base:
            ----------------------------------------------------
            %s
            ----------------------------------------------------
            Using only the context above, answer the student's question. If the answer is not in the
            context, say you are not sure and suggest who to contact.

            Question: %s
            """.formatted(context, question); //may change this, this is the final userPrompt that is sent to the LLM

        // 3. GENERATE: ask the LLM for a grounded answer.
        String answer = llm.complete(systemPrompt, userPrompt); //returns the LLM API's response
        return new RagResult(context, systemPrompt, answer);
    }
}
