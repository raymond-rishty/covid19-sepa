package rrishty.covid19sepa

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler

class LambdaHandler  : RequestHandler<Request, String> {
    override fun handleRequest(input: Request, context: Context): String {
        check(input.regionName.isNotBlank()) { "regionName is required but was not received" }
        Application.execute(input.regionName, context.logger)
        return "200 OK"
    }
}


data class Request(var regionName: String = "")
