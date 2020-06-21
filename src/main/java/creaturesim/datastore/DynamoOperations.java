package creaturesim.datastore;

import org.joda.time.Instant;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.net.URI;
import java.util.HashMap;
import java.util.UUID;

public class DynamoOperations {

    private static DynamoDbClient client;
    private static final String creatureTableName = "Creatures";

    public DynamoOperations(String dynamoHost, String dynamoRegion){
        URI hostURI = URI.create(dynamoHost);

        if(dynamoHost.contains("localhost")) {
            client = DynamoDbClient.builder().region(selectRegion(dynamoRegion)).endpointOverride(hostURI).build();
        } else {
            client = DynamoDbClient.builder()
                    .region(selectRegion(dynamoRegion))
                    .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                    .build();
        }
    }

    public String createNewCreature(String creatureName, String creatorName, int strength, int attack, int defence,
                                    int speed, String avatarHash, String parentOneGUID, String parentTwoGUID){

        String creatureGUID = UUID.randomUUID().toString();

        HashMap<String, AttributeValue> itemValues = new HashMap<>();
        itemValues.put("CreatureId", AttributeValue.builder().s(creatureGUID).build());
        itemValues.put("CreatureName", AttributeValue.builder().s(creatureName).build());
        itemValues.put("CreatorName", AttributeValue.builder().s(creatorName).build());
        itemValues.put("Strength", AttributeValue.builder().n(String.valueOf(strength)).build());
        itemValues.put("Attack", AttributeValue.builder().n(String.valueOf(attack)).build());
        itemValues.put("Defence", AttributeValue.builder().n(String.valueOf(defence)).build());
        itemValues.put("Speed", AttributeValue.builder().n(String.valueOf(speed)).build());
        itemValues.put("AvatarHash", AttributeValue.builder().s(avatarHash).build());
        itemValues.put("Created", AttributeValue.builder().n(String.valueOf(Instant.now().getMillis())).build());

        if(parentOneGUID != null && parentTwoGUID != null) {
            itemValues.put("ParentOne", AttributeValue.builder().s(parentOneGUID).build());
            itemValues.put("ParentTwo", AttributeValue.builder().s(parentTwoGUID).build());
        }
        itemValues.put("Timeline", AttributeValue.builder().l(
                AttributeValue.builder().m(
                        new HashMap<String, AttributeValue>(){{
                            put(String.valueOf(Instant.now().getMillis()),AttributeValue.builder().s("Born").build());
                        }}
                ).build()
        ).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(creatureTableName)
                .item(itemValues)
                .build();

        try {
            client.putItem(request);
            return creatureGUID;
        }
        catch (DynamoDbException e) {
            System.err.println(String.format("Could not create item: %s", e.getMessage()));
            return null;
        }
    }

    public ScanResponse getAllCreatures(){
        try{
            ScanRequest req = ScanRequest.builder()
                    .tableName(creatureTableName)
                    .build();
            return client.scan(req);
        }
        catch (DynamoDbException e){
            System.err.println(String.format("Could not get items: %s", e.getMessage()));
            return null;
        }
    }

    private Region selectRegion(String region){
        if(region.equalsIgnoreCase("eu-west-1")){
            return Region.EU_WEST_1;
        }else {
            return Region.US_EAST_2;
        }
    }
}
