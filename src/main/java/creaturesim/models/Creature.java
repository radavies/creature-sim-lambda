package creaturesim.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Creature {
    @JsonProperty
    public String creatureId;
    @JsonProperty
    public String creatureName;
    @JsonProperty
    public String creatorName;
    @JsonProperty
    public int strengthPoints;
    @JsonProperty
    public int attackPoints;
    @JsonProperty
    public int defencePoints;
    @JsonProperty
    public int speedPoints;
    @JsonProperty
    public String avatarHash;

    public Creature(){

    }

    public Creature(String creatureId, String creatureName, String creatorName, int strengthPoints, int attackPoints, int defencePoints, int speedPoints, String avatarHash){
        this.creatureId = creatureId;
        this.creatureName = creatureName;
        this.creatorName = creatorName;
        this.strengthPoints = strengthPoints;
        this.attackPoints = attackPoints;
        this.defencePoints = defencePoints;
        this.speedPoints = speedPoints;
        this.avatarHash = avatarHash;
    }
}
