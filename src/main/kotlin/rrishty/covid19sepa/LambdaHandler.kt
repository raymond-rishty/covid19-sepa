package rrishty.covid19sepa

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler

class LambdaHandler  : RequestHandler<Request, String> {
    override fun handleRequest(input: Request?, context: Context): String {
        Application.execute(context.logger)
        return "200 OK"
    }
}


class Request
