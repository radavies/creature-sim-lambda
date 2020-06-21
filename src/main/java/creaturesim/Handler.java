package creaturesim;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import com.amazonaws.services.lambda.runtime.events.ScheduledEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import creaturesim.datastore.DynamoObjectMapper;
import creaturesim.datastore.DynamoOperations;
import creaturesim.models.Creature;

import java.util.ArrayList;
import java.util.Map;

// Handler value: example.Handler
public class Handler implements RequestHandler<ScheduledEvent, String>{
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    @Override
    public String handleRequest(ScheduledEvent event, Context context)
    {
        LambdaLogger logger = context.getLogger();
        String response = new String("200 OK");
        // log execution details
        logger.log("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()));
        logger.log("CONTEXT: " + gson.toJson(context));
        // process event
        logger.log("EVENT: " + gson.toJson(event));
        logger.log("EVENT TYPE: " + event.getClass().toString());
        logger.log("RAYMOND IS COOL");

        DynamoOperations dynamo = new DynamoOperations("aws", "eu-west-1");
        DynamoObjectMapper mapper = new DynamoObjectMapper();
        ArrayList<Creature> creatures = mapper.MapCreatures(dynamo.getAllCreatures());

        logger.log("PRINTING CREATURES");
        logger.log(gson.toJson(creatures));

        return response;
    }
}