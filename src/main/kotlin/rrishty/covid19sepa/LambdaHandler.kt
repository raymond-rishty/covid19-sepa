package rrishty.covid19sepa

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler

class LambdaHandler  : RequestHandler<String, String> {
    override fun handleRequest(input: String?, context: Context): String {
        Application.execute(context.logger)
        return "200 OK"
    }
}