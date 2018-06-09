package net.swiftpk.client.entity;

public class Actor {

	public int animationCount[];

	public int anInt176;
	public int attackingCameraInt;
	public int attackingMobIndex;
	public int attackingNpcIndex;
	public int colorBottomType;
	public int colorHairType;
	public int colorSkinType;
	public int colorTopType;
	public int currentAnimation;
	public int currentDamage;
	public String currentMessage;
	public int currentX;
	public int currentY;
	public int group;
	public int healthBarTimer;
	public int hitPointsBase;
	public int hitPointsCurrent;
	public int id;
	public int isSkulled;
	public int itemBubbleDelay;
	public int itemBubbleId;
	public int lastMessageTimeout;
	public int level;
	public int mobIntUnknown;
	public String name;
	public long nameLong;
	public int nextSprite;
	public int serverIndex;
	public int stepCount;
	public boolean unusedBool;
	public int unusedInt;
	public int waypointCurrent;
	public int waypointEndSprite;
	public int waypointsX[];
	public int waypointsY[];

	public String clanName;
	public int cape = 6908265;

	public Actor() {
		waypointsX = new int[10];
		waypointsY = new int[10];
		animationCount = new int[12];
		level = -1;
		unusedBool = false;
		unusedInt = -1;
	}
}
