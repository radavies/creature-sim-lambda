package creaturesim.datastore;

import creaturesim.models.Creature;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.util.ArrayList;
import java.util.Map;

public class DynamoObjectMapper {
    public DynamoObjectMapper(){

    }

    public ArrayList<Creature> MapCreatures(ScanResponse creatureScan){
        ArrayList<Creature> list = new ArrayList<>();

        if(creatureScan == null || creatureScan.count() == 0){
            return null;
        }

        for(Map<String, AttributeValue> creatureItem : creatureScan.items()){
            list.add(MapCreature(creatureItem));
        }

        return list;
    }

    public Creature MapCreature(Map<String, AttributeValue> creatureItem){
        String creatureId = creatureItem.get("CreatureId").s();
        String creatureName = creatureItem.get("CreatureName").s();
        String creatorName = creatureItem.get("CreatorName").s();
        int strengthPoints = Integer.parseInt(creatureItem.get("Strength").n());
        int attackPoints = Integer.parseInt(creatureItem.get("Attack").n());
        int defencePoints = Integer.parseInt(creatureItem.get("Defence").n());
        int speedPoints = Integer.parseInt(creatureItem.get("Speed").n());
        String avatarHash = creatureItem.get("AvatarHash").s();

        return new Creature(creatureId, creatureName, creatorName, strengthPoints, attackPoints, defencePoints, speedPoints, avatarHash);
    }

}
