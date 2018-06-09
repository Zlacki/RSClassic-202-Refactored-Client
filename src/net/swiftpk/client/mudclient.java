package net.swiftpk.client;

import net.swiftpk.client.audio.SoundInputStream;
import net.swiftpk.client.bzip.DataFileDecrypter;
import net.swiftpk.client.cache.Archive;
import net.swiftpk.client.cache.ChatMessage;
import net.swiftpk.client.cache.Data;
import net.swiftpk.client.cache.Utils;
import net.swiftpk.client.entity.Actor;
import net.swiftpk.client.gfx.*;
import net.swiftpk.client.gfx.Menu;
import net.swiftpk.client.gfx.uis.BankUI;
import net.swiftpk.client.gfx.uis.various.GameUIs;
import net.swiftpk.client.io.ServerPacket;
import net.swiftpk.client.io.StreamClass;
import net.swiftpk.client.loader.various.AppletUtils;
import net.swiftpk.client.packethandler.NPCPositionUpdate;
import net.swiftpk.client.packethandler.PacketHandler;
import net.swiftpk.client.packethandler.PlayerPositionUpdate;
import net.swiftpk.client.packethandler.sleep.FatigueUpdate;
import net.swiftpk.client.packethandler.sleep.SleepFatigueUpdate;
import net.swiftpk.client.packethandler.sleep.SleepIncorrectAnswer;
import net.swiftpk.client.scene.GameModel;
import net.swiftpk.client.scene.Scene;
import net.swiftpk.client.terrian.World;
import net.swiftpk.client.util.ImplementationDelegate;
import net.swiftpk.client.util.Utility;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

import static net.swiftpk.client.util.GameConstants.*;

public class mudclient<Delegate_T extends ImplementationDelegate> extends GameNetworking<ImplementationDelegate> {
    public static mudclient<?> getInstance() {
        return mudclient;
    }

    public static void main(String args[]) {
        GameFrame gameFrame = new GameFrame(512, 334 + 11);
    }

    protected static final Socket makeSecureSocket(String s, int i) throws IOException {
        Socket socket1 = new Socket();
        socket1.setSoTimeout(5000);
        socket1.setTcpNoDelay(true);
        socket1.connect(new InetSocketAddress(s, i), 5000);
        return socket1;
    }

    private static String itemAmountToString(int i) {
        String s = String.valueOf(i);
        for(int j = s.length() - 3; j > 0; j -= 3)
            s = s.substring(0, j) + "," + s.substring(j);
        if(s.length() > 8)
            s = "@gre@" + s.substring(0, s.length() - 8) + " million @whi@(" + s + ")";
        else if(s.length() > 4)
            s = "@cya@" + s.substring(0, s.length() - 4) + "K @whi@(" + s + ")";
        return s;
    }

    private static final int[][] ANIMATIONS;
    private static final boolean FOG_ENABLED;
    private static final float FPS = 15;
    private static final int[] HAIR_COLORS, SKIN_COLORS, CLOTHES_COLORS, COMBAT_ANIMATIONS_RIGHT, COMBAT_ANIMATIONS_LEFT, WALK_ANIMATIONS;
    private static final int PROJECTILE_SPEED, SPRITE_MEDIA, SPRITE_UTIL;
    public static final int SPRITE_ITEM;
    private static final int SPRITE_LOGO;
    private static final int SPRITE_PROJECTILE;
    private static final int SPRITE_TEXTURE;
    private static final int SPRITE_TEXTURE_WORLD;
    private static final String[] SKILL_NAMES, EQUIPMENT_INFO;
    public static long PING_RECIEVED, PING_SENT;
    private static mudclient<?> mudclient;

    static {
        HAIR_COLORS = new int[] { 0xffc030, 0xffa040, 0x805030, 0x604020, 0x303030, 0xff6020, 0xff4000, 0xffffff, 65280, 65535 };
        SKIN_COLORS = new int[] { 0xecded0, 0xccb366, 0xb38c40, 0x997326, 0x906020 };
        CLOTHES_COLORS = new int[] { 0xff0000, 0xff8000, 0xffe000, 0xa0e000, 57344, 32768, 41088, 45311, 33023, 12528, 0xe000e0, 0x303030, 0x604000, 0x805000, 0xffffff };
        COMBAT_ANIMATIONS_RIGHT = new int[] { 0, 0, 0, 0, 0, 1, 2, 1 };
        COMBAT_ANIMATIONS_LEFT = new int[] { 0, 1, 2, 1, 0, 0, 0, 0 };
        WALK_ANIMATIONS = new int[] { 0, 1, 2, 1 };
        ANIMATIONS = new int[][] { { 11, 2, 9, 7, 1, 6, 10, 0, 5, 8, 3, 4 }, { 11, 2, 9, 7, 1, 6, 10, 0, 5, 8, 3, 4 }, { 11, 3, 2, 9, 7, 1, 6, 10, 0, 5, 8, 4 }, { 3, 4, 2, 9, 7, 1, 6, 10, 8, 11, 0, 5 }, { 3, 4, 2, 9, 7, 1, 6, 10, 8, 11, 0, 5 }, { 4, 3, 2, 9, 7, 1, 6, 10, 8, 11, 0, 5 }, { 11, 4, 2, 9, 7, 1, 6, 10, 0, 5, 8, 3 }, { 11, 2, 9, 7, 1, 6, 10, 0, 5, 8, 4, 3 } };
        SKILL_NAMES = new String[] { "Attack", "Defense", "Strength", "Hits", "Ranged", "Prayer", "Magic", "Cooking", "Woodcut", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining", "Herblaw", "Agility", "Thieving" };
        EQUIPMENT_INFO = new String[] { "Armour", "WeaponAim", "WeaponPower", "Magic", "Prayer", "Range" };
        PROJECTILE_SPEED = 40;
        PING_RECIEVED = -1;
        PING_SENT = -1;
        SPRITE_MEDIA = 2000;
        SPRITE_UTIL = SPRITE_MEDIA + 100;
        SPRITE_ITEM = SPRITE_UTIL + 50;
        SPRITE_LOGO = SPRITE_ITEM + 1000;
        SPRITE_PROJECTILE = SPRITE_LOGO + 10;
        SPRITE_TEXTURE = SPRITE_PROJECTILE + 50;
        SPRITE_TEXTURE_WORLD = SPRITE_TEXTURE + 10;
        FOG_ENABLED = false;
    }

    private int captchaPixels[][];
    private int captchaWidth;
    private int captchaHeight;
    public String sleepScreenMessage;
    private boolean sleeping;
    public int petInventoryCount = 0;
    public int[] petInventoryItem = new int[20];
    public int[] petInventoryQunant = new int[20];
    private boolean showPetInventory = false;
    private final boolean aBooleanArray827[];
    private final boolean aBooleanArray970[];
    private int abuseSelectedType;
    private int actionPictureType;
    int actionPictureX;
    int actionPictureY;
    private Graphics aGraphics936;
    private boolean allowSendCommand = false;
    private int animationNumber;
    private int animationZeroCount;
    public int fatigue;
    public int sleepFatigue;
    private int anInt658;
    private int anInt699;
    private int anInt718;
    private int anInt727;
    private int anInt742;
    private int anInt743;
    private int anInt744;
    private int anInt789;
    private int anInt790;
    private int anInt791;
    private int anInt792;
    int anInt826;
    private final int anInt882;
    private int anInt892;
    private int anInt900;
    private int anInt911;
    private int anInt952;
    private int anInt953;
    private int anInt954;
    private int anInt955;
    int anInt981;
    private int anInt985;
    private int anInt986;
    private final int anIntArray705[];
    private final int anIntArray706[];
    private final int anIntArray757[];
    private final int anIntArray782[];
    private final int anIntArray858[];
    private final int anIntArray859[];
    private final int anIntArray923[];
    private final int anIntArray944[];
    public int areaX;
    public int areaY;
    private SoundInputStream soundInputStream;
    private int cameraAutoAngle;
    private final boolean cameraAutoAngleDebug;
    private int cameraHeight;
    private int cameraRotation;
    private int cameraRotationBaseAddition;
    public int cameraSizeInt;
    private final int character2Color;
    private int characterBodyGender;
    private int characterBottomColor;
    private int characterDesignAcceptButton;
    private int characterDesignBottomColorButton1;
    private int characterDesignBottomColorButton2;
    private int characterDesignGenderButton1;
    private int characterDesignGenderButton2;
    private int characterDesignHairColorButton1;
    private int characterDesignHairColorButton2;
    private int characterDesignHeadButton1;
    private int characterDesignHeadButton2;
    private Menu characterDesignMenu;
    private int characterDesignSkinColorButton1;
    private int characterDesignSkinColorButton2;
    private int characterDesignTopColorButton1;
    private int characterDesignTopColorButton2;
    private int characterHairColor;
    private int characterHeadGender;
    private int characterHeadType;
    private boolean characterLooksScreenChanged = false, showIGPing = false;
    private int characterSkinColor;
    private int characterTopColor;
    int chatInputHandle;
    private boolean clickScreenSend = false;
    private int combatStyle;
    private boolean configAutoCameraAngle;
    private boolean configMouseButtons;
    private boolean configSoundEffects;
    private int currentChat;
    private String currentPass;
    private String currentUser;
    private boolean deposit;
    public boolean rightClickOptions;
    public boolean valueSet;
    private boolean notInWilderness;
    private final DecimalFormat df = new DecimalFormat("00"), df2 = new DecimalFormat("0.00");
    private int duelCantRetreat;
    private int duelConfirmMyItemCount;
    private final int duelConfirmMyItems[];
    private final int duelConfirmMyItemsCount[];
    private int duelConfirmOpponentItemCount;
    private final int duelConfirmOpponentItems[];
    private final int duelConfirmOpponentItemsCount[];
    private boolean duelMyAccepted;
    private int duelMyItemCount;
    private final int duelMyItems[];
    private final int duelMyItemsCount[];
    private boolean duelNoMagic;
    private boolean duelNoPrayer;
    private boolean duelNoRetreating;
    private boolean duelNoWeapons;
    private boolean duelOpponentAccepted;
    private int duelOpponentItemCount;
    private final int duelOpponentItems[];
    private final int duelOpponentItemsCount[];
    private String duelOpponentName;
    private long duelOpponentNameLong;
    private int duelUseMagic;
    private int duelUsePrayer;
    private int duelUseWeapons;
    private boolean duelWeAccept;
    private final int equipmentStatus[];
    private final boolean errorUnableToLoad;
    private long experienceArray[];
    private int fightCount;
    private Menu friendsMenu;
    int friendsMenuHandle;
    private Menu gameMenu;
    private GameModel gameModels[];
    private int groundItemCount;
    private final int groundItemObjectVar[];
    private final int groundItemType[];
    private final int groundItemX[];
    private final int groundItemY[];
    private boolean hasReceivedWelcomeBoxDetails;
    private final int healthBarPercentages[];
    private final int healthBarX[];
    private final int healthBarY[];
    public int inputBoxType;
    private int inputID;
    public int tradeWindowX;
    public int tradeWindowY;
    private int inventoryCount;
    private final int inventoryItems[];
    private final int inventoryItemsCount[];
    boolean isRecording = false;
    private int itemIncrement;
    public int knownPlayerCount;
    public Actor knownPlayers[];
    private int lastAutoCameraRotatePlayerX;
    private int lastAutoCameraRotatePlayerY;
    private boolean lastLoadedNull;
    private String lastLoggedInAddress;
    public Actor lastNpcArray[];
    public int lastNpcCount;
    private long lastTradeDuelUpdate = System.currentTimeMillis();
    private int lastWalkTimeout;
    private int lastWildYSubtract;
    public int loggedIn;
    private int loginButtonExistingUser;
    private int loginButtonNewUser;
    private int loginCancelButton;
    private int loginOkButton;
    private int loginPasswordTextBox;
    private int loginScreenNumber;
    private int loginStatusText;
    private int loginTimer;
    private int loginUsernameTextBox;
    private int logoutTimeout;
    private final int magicLoc;
    private int magicMenuIndex;
    private final boolean member;
    private boolean memoryError;
    private final int menuActionType[];
    public int menuActionVariable[];
    public int menuActionVariable2[];
    private final int menuActionX[];
    private final int menuActionY[];
    private int menuHeight;
    public int menuID[];
    private final int menuIndexes[];
    public int menuLength;
    private Menu menuLogin;
    int menuMagicPrayersSelected;
    private Menu menuNewUser;
    public String menuText1[];
    public String menuText2[];
    private Menu menuWelcome;
    public int menuWidth;
    private int menuX;
    private int menuY;
    private final List<String> messages;
    private final String messagesArray[];
    int generalChatHandle;
    int questChatHandle;
    int messagesHandleType6;
    int messagesTab;
    private final int messagesTimeout[];
    public Actor mobArray[];
    public int mobArrayIndexes[];
    private int mobMessageCount;
    String mobMessages[];
    private final int mobMessagesHeight[];
    private final int mobMessagesWidth[];
    private final int mobMessagesX[];
    private final int mobMessagesY[];
    private int modelClawSpellNumber;
    private int modelFireLightningSpellNumber;
    private int modelTorchNumber;
    private int modelUpdatingTimer;
    public int mouseButtonClick;
    private int mouseClickArrayOffset;
    int mouseClickXArray[];
    int mouseClickYArray[];
    private int mouseDownTime;
    private int mouseOverMenu;
    private int newUserOkButton;
    public int npcCount;
    private Actor npcRecordArray[];
    public Actor npcs[];
    private int objectCount;
    private final int objectID[];
    private GameModel objectModelArray[];
    private final int objectType[];
    private final int objectX[];
    private final int objectY[];
    public Actor ourPlayer;
    public int ourPlayerServerIndex;
    private int playerAliveTimeout;
    public Actor playerArray[];
    public int playerCount;
    private final int playerStatBase[];
    private final int playerStatCurrent[];
    private final long playerStatExperience[];
    private int prayerMenuIndex;
    private final boolean prayerOn[];
    long privateMessageTarget;
    private final String questionMenuAnswer[];
    private int questionMenuCount;
    private int referId;
    public int regionX;
    public int regionY;
    private Scene scene;
    private int screenRotationTimer;
    private int screenRotationX;
    private int screenRotationY;
    private final int sectionXArray[];
    private final int sectionYArray[];
    private int selectedItem;
    String selectedItemName;
    private int selectedShopItemIndex;
    private int selectedShopItemType;
    private int selectedSpell;
    private String serverLocation = "";
    private String serverMessage;
    private boolean serverMessageBoxTop;
    private long serverStartTime = 0;
    private final int shopItemBasePriceModifier[];
    private int shopItemBuyPriceModifier;
    private final int shopItemCount[];
    private final int shopItems[];
    private int shopItemSellPriceModifier;
    private int showAbuseWindow;
    private boolean showCharacterLookScreen;
    private boolean showDuelConfirmWindow;
    private boolean showDuelWindow;
    private boolean showQuestionMenu;
    private boolean showRightClickMenu;
    private final boolean showRoofs = false;
    private boolean showServerMessageBox;
    private boolean showShop;
    private boolean showTradeConfirmWindow;
    private boolean showTradeWindow;
    private byte soundFilesArchiveData[];
    private Menu spellMenu;
    int spellMenuHandle;
    public SurfaceSprite surface;
    private int systemUpdate;
    private boolean tradeConfirmAccepted;
    private int tradeConfirmItemCount;
    private final int tradeConfirmItems[];
    private final int tradeConfirmItemsCount[];
    private int tradeConfirmOtherItemCount;
    private final int tradeConfirmOtherItems[];
    private final int tradeConfirmOtherItemsCount[];
    private long tradeConfirmOtherNameLong;
    private int tradeMyItemCount;
    private final int tradeMyItems[];
    private final int tradeMyItemsCount[];
    private boolean tradeOtherAccepted;
    private int tradeOtherItemCount;
    private final int tradeOtherItems[];
    private final int tradeOtherItemsCount[];
    private String tradeOtherPlayerName;
    private boolean tradeWeAccepted;
    private int wallObjectCount;
    private final int wallObjectDirection[];
    private GameModel wallObjectModel[];
    private final int wallObjectType[];
    private final int wallObjectX[];
    private final int wallObjectY[];
    private final int wearing[];
    private int wildernessType;
    private int wildX;
    private int wildY;
    private int wildYMultiplier;
    private int currentPlane;
    public int windowHeight;
    public int windowWidth;
    private World world;
    private int playersOnline = 0;
    private boolean zoomCamera;
    private final int shopItemsBuyPrice[];
    private final int shopItemsSellPrice[];
    public int gameWidth;
    public int gameHeight;
    public int bankSize;
    private final Object sync_on_me = new Object();
    private boolean shouldResize = false;
    private int resizeToW;
    private int resizeToH;
    private int autoSpell;

    public mudclient(Delegate_T container, int width, int height) {
        super(container);
        mudclient = this;
        gameWidth = width;
        gameHeight = height;
        captchaPixels = new int[0][0];
        captchaWidth = 0;
        captchaHeight = 0;
        sleepScreenMessage = null;
        sleeping = false;
        duelMyItems = new int[8];
        duelMyItemsCount = new int[8];
        configAutoCameraAngle = true;
        questionMenuAnswer = new String[10];
        lastNpcArray = new Actor[500];
        currentUser = "";
        currentPass = "";
        menuText1 = new String[250];
        duelOpponentAccepted = false;
        duelMyAccepted = false;
        tradeConfirmItems = new int[14];
        tradeConfirmItemsCount = new int[14];
        tradeConfirmOtherItems = new int[14];
        tradeConfirmOtherItemsCount = new int[14];
        serverMessage = "";
        duelOpponentName = "";
        inventoryItems = new int[35];
        inventoryItemsCount = new int[35];
        wearing = new int[35];
        mobMessages = new String[50];
        wallObjectModel = new GameModel[500];
        mobMessagesX = new int[50];
        mobMessagesY = new int[50];
        mobMessagesWidth = new int[50];
        mobMessagesHeight = new int[50];
        npcs = new Actor[500];
        equipmentStatus = new int[6];
        prayerOn = new boolean[50];
        tradeOtherAccepted = false;
        tradeWeAccepted = false;
        mobArray = new Actor[4000];
        anIntArray705 = new int[50];
        anIntArray706 = new int[50];
        lastWildYSubtract = -1;
        memoryError = false;
        showQuestionMenu = false;
        magicLoc = 128;
        cameraAutoAngle = 1;
        anInt727 = 2;
        showServerMessageBox = false;
        hasReceivedWelcomeBoxDetails = false;
        playerStatCurrent = new int[18];
        currentPlane = -1;
        anInt742 = -1;
        anInt743 = -1;
        anInt744 = -1;
        sectionXArray = new int[8000];
        sectionYArray = new int[8000];
        selectedItem = -1;
        selectedItemName = "";
        duelOpponentItems = new int[8];
        duelOpponentItemsCount = new int[8];
        anIntArray757 = new int[50];
        menuID = new int[250];
        showCharacterLookScreen = false;
        knownPlayers = new Actor[500];
        gameModels = new GameModel[1000];
        configMouseButtons = false;
        duelNoRetreating = false;
        duelNoMagic = false;
        duelNoPrayer = false;
        duelNoWeapons = false;
        anIntArray782 = new int[50];
        duelConfirmOpponentItems = new int[8];
        duelConfirmOpponentItemsCount = new int[8];
        healthBarX = new int[50];
        healthBarY = new int[50];
        healthBarPercentages = new int[50];
        objectModelArray = new GameModel[1500];
        cameraRotation = 128;
        characterBodyGender = 1;
        character2Color = 2;
        characterHairColor = 2;
        characterTopColor = 8;
        characterBottomColor = 14;
        characterHeadGender = 1;
        menuText2 = new String[250];
        aBooleanArray827 = new boolean[1500];
        playerStatBase = new int[18];
        menuActionType = new int[250];
        menuActionVariable = new int[250];
        menuActionVariable2 = new int[250];
        shopItems = new int[256];
        shopItemCount = new int[256];
        shopItemBasePriceModifier = new int[256];
        shopItemsSellPrice = new int[256];
        shopItemsBuyPrice = new int[256];
        // member = false;
        member = true;
        anIntArray858 = new int[50];
        anIntArray859 = new int[50];
        duelConfirmMyItems = new int[8];
        duelConfirmMyItemsCount = new int[8];
        mobArrayIndexes = new int[500];
        messagesTimeout = new int[5];
        objectX = new int[1500];
        objectY = new int[1500];
        objectType = new int[1500];
        objectID = new int[1500];
        menuActionX = new int[250];
        menuActionY = new int[250];
        ourPlayer = new Actor();
        ourPlayerServerIndex = -1;
        anInt882 = 30;
        showTradeConfirmWindow = false;
        tradeConfirmAccepted = false;
        playerArray = new Actor[500];
        serverMessageBoxTop = false;
        cameraHeight = 650;
        selectedSpell = -1;
        anInt911 = 2;
        tradeOtherItems = new int[14];
        tradeOtherItemsCount = new int[14];
        menuIndexes = new int[250];
        zoomCamera = false;
        playerStatExperience = new long[18];
        cameraAutoAngleDebug = false;
        npcRecordArray = new Actor[5000];
        showDuelWindow = false;
        anIntArray923 = new int[50];
        lastLoadedNull = false;
        showShop = false;
        mouseClickXArray = new int[8192];
        mouseClickYArray = new int[8192];
        showDuelConfirmWindow = false;
        duelWeAccept = false;
        wallObjectX = new int[500];
        wallObjectY = new int[500];
        configSoundEffects = false;
        showRightClickMenu = false;
        anIntArray944 = new int[50];
        wallObjectDirection = new int[500];
        wallObjectType = new int[500];
        groundItemX = new int[5000];
        groundItemY = new int[5000];
        groundItemType = new int[5000];
        groundItemObjectVar = new int[5000];
        selectedShopItemIndex = -1;
        selectedShopItemType = -2;
        messagesArray = new String[5];
        showTradeWindow = false;
        aBooleanArray970 = new boolean[500];
        tradeMyItems = new int[14];
        tradeMyItemsCount = new int[14];
        windowWidth = 512;
        windowHeight = 334;
        cameraSizeInt = 9;
        errorUnableToLoad = false;
        tradeOtherPlayerName = "";
        deposit = false;
        rightClickOptions = false;
        valueSet = false;
        tradeWindowX = tradeWindowY = 0;
        notInWilderness = false;
        messages = new ArrayList<>();
        if(AppletUtils.isApplet)
            onResize(width, height);
    }

    private void addDuelItems(int actionVariable, int actionVariable2, int actionType, boolean offerx) {
        if(duelMyItemCount > 8)
            return;
        int currentStack = inventoryCount(actionVariable);
        int currentDuelItemCount = 0;
        for(int c = 0; c < duelMyItemCount; c++)
            if(Data.itemStackable[actionVariable] == 0) {
                if(duelMyItems[c] == actionVariable) {
                    currentDuelItemCount = duelMyItemsCount[c];
                    currentStack = inventoryCount(actionVariable) - duelMyItemsCount[c];
                }
            } else {
                if(duelMyItems[c] == actionVariable)
                    currentDuelItemCount++;
            }
        if(currentDuelItemCount + actionVariable2 < 0 && Data.itemStackable[actionVariable] == 1)
            return;
        if(currentDuelItemCount + actionVariable2 > inventoryCount(actionVariable))
            actionVariable2 = inventoryCount(actionVariable) - currentDuelItemCount;
        if(currentDuelItemCount + actionVariable2 < 0 && Data.itemStackable[actionVariable] == 0) {
            actionVariable2 = currentStack;
            actionType = 1234;
        }
        if(actionType == 1234 && Data.itemStackable[actionVariable] == 0)
            actionVariable2 = currentStack;
        if(currentStack == 0)
            return;
        boolean done = false;
        for(int c = 0; c < duelMyItemCount; c++)
            if(duelMyItems[c] == actionVariable && Data.itemStackable[actionVariable] == 0) {
                duelMyItemsCount[c] += actionVariable2;
                done = true;
                break;
            }
        int count = 0;
        if(inventoryCount(actionVariable) < actionVariable2) {
            if(inventoryCount(actionVariable) - count < 1)
                return;
            else if(inventoryCount(actionVariable) - count >= 1)
                actionVariable2 = inventoryCount(actionVariable);
        }
        if(Data.itemStackable[actionVariable] == 1) {
            for(int c = 0; c < duelMyItemCount; c++)
                if(duelMyItems[c] == actionVariable)
                    count++;
            int freeSlots = 8 - duelMyItemCount;
            if(actionVariable2 > freeSlots)
                actionVariable2 = freeSlots;
            for(int c = 0; c < actionVariable2 - 1; c++) {
                duelMyItems[duelMyItemCount] = actionVariable;
                duelMyItemsCount[duelMyItemCount] = actionVariable2;
                duelMyItemCount++;
            }
        }
        if(!done && inventoryCount(actionVariable) - count >= 1) {
            duelMyItems[duelMyItemCount] = actionVariable;
            duelMyItemsCount[duelMyItemCount] = actionVariable2;
            duelMyItemCount++;
        }
        super.streamClass.addNewFrame(170);
        super.streamClass.addByte(duelMyItemCount);
        for(int c = 0; c < duelMyItemCount; c++) {
            super.streamClass.addShort(duelMyItems[c]);
            super.streamClass.addInt(duelMyItemsCount[c]);
        }
        super.streamClass.formatCurrentFrame();
        duelOpponentAccepted = false;
        duelMyAccepted = false;
    }

    private GameModel addModel(int x, int y, int k, int l, int i1) {
        int modelX = x;
        int modelY = y;
        int modelX1 = x;
        int modelX2 = y;
        int j2 = Data.doorModelVar2[l];
        int k2 = Data.doorModelVar3[l];
        int l2 = Data.doorModelVar1[l];
        GameModel model = new GameModel(4, 1);
        if(k == 0)
            modelX1 = x + 1;
        if(k == 1)
            modelX2 = y + 1;
        if(k == 2) {
            modelX = x + 1;
            modelX2 = y + 1;
        }
        if(k == 3) {
            modelX1 = x + 1;
            modelX2 = y + 1;
        }
        modelX *= magicLoc;
        modelY *= magicLoc;
        modelX1 *= magicLoc;
        modelX2 *= magicLoc;
        int i3 = model.vertexAt(modelX, -world.getElevation(modelX, modelY), modelY);
        int j3 = model.vertexAt(modelX, -world.getElevation(modelX, modelY) - l2, modelY);
        int k3 = model.vertexAt(modelX1, -world.getElevation(modelX1, modelX2) - l2, modelX2);
        int l3 = model.vertexAt(modelX1, -world.getElevation(modelX1, modelX2), modelX2);
        int ai[];
        ai = new int[] { i3, j3, k3, l3 };
        model.makeFace(4, ai, j2, k2);
        model.setLight(false, 60, 24, -50, -10, -50);
        if(x >= 0 && y >= 0 && x < 96 && y < 96)
            scene.addModel(model);
        model.key = i1 + 10000;
        return model;
    }

    public final Actor addNPC(int serverIndex, int x, int y, int nextSprite, int type) {
        if(npcRecordArray[serverIndex] == null) {
            npcRecordArray[serverIndex] = new Actor();
            npcRecordArray[serverIndex].serverIndex = serverIndex;
        }
        Actor mob = npcRecordArray[serverIndex];
        boolean npcAlreadyExists = false;
        for(int lastNpcIndex = 0; lastNpcIndex < lastNpcCount; lastNpcIndex++) {
            if(lastNpcArray[lastNpcIndex].serverIndex != serverIndex)
                continue;
            npcAlreadyExists = true;
            break;
        }
        if(npcAlreadyExists) {
            mob.id = type;
            mob.nextSprite = nextSprite;
            int waypointCurrent = mob.waypointCurrent;
            if(x != mob.waypointsX[waypointCurrent] || y != mob.waypointsY[waypointCurrent]) {
                mob.waypointCurrent = waypointCurrent = (waypointCurrent + 1) % 10;
                mob.waypointsX[waypointCurrent] = x;
                mob.waypointsY[waypointCurrent] = y;
            }
        } else {
            mob.serverIndex = serverIndex;
            mob.waypointEndSprite = 0;
            mob.waypointCurrent = 0;
            mob.waypointsX[0] = mob.currentX = x;
            mob.waypointsY[0] = mob.currentY = y;
            mob.id = type;
            mob.nextSprite = mob.currentAnimation = nextSprite;
            mob.stepCount = 0;
        }
        npcs[npcCount++] = mob;
        return mob;
    }

    @Override
    public long getUID() {
        File uID = new File(AppletUtils.CACHE + File.separator + "uid.dat");
        try {
            if(!uID.exists()) {
                PrintWriter printWriter = new PrintWriter(new FileOutputStream(uID), true);
                long uuID = new SecureRandom().nextLong();
                printWriter.println(uuID);
                printWriter.flush();
                uID.setReadOnly();
                return uuID;
            } else {
                if(uID.canWrite()) {
                    PrintWriter printWriter = new PrintWriter(new FileOutputStream(uID), true);
                }
                long theUID;
                try(BufferedReader buffer = new BufferedReader(new FileReader(uID))) {
                    theUID = Long.parseLong(buffer.readLine());
                }
                return theUID;
            }
        } catch(IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public String getMacAddress() throws SocketException {
        String firstInterface = null;
        Map<String, String> addressByNetwork = new HashMap<>();
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

        while(networkInterfaces.hasMoreElements()) {
            NetworkInterface network = networkInterfaces.nextElement();

            byte[] bmac = network.getHardwareAddress();
            if(bmac != null) {
                StringBuilder sb = new StringBuilder();
                for(int i = 0; i < bmac.length; i++) {
                    sb.append(String.format("%02X%s", bmac[i], (i < bmac.length - 1) ? "-" : ""));
                }

                if(sb.toString().isEmpty() == false) {
                    addressByNetwork.put(network.getName(), sb.toString());
                }

                if(sb.toString().isEmpty() == false && firstInterface == null) {
                    firstInterface = network.getName();
                }
            }
        }

        if(firstInterface != null) {
            return addressByNetwork.get(firstInterface);
        }

        return null;
    }

    private void addTradeItems(int actionVariable, int actionVariable2, int actionType, boolean offerx) {
        if(tradeMyItemCount > 11)
            return;
        int currentStack = inventoryCount(actionVariable);
        int tradeCount = 0;
        for(int c = 0; c < tradeMyItemCount; c++)
            if(Data.itemStackable[actionVariable] == 0) {
                if(tradeMyItems[c] == actionVariable) {
                    tradeCount = tradeMyItemsCount[c];
                    currentStack = inventoryCount(actionVariable) - tradeMyItemsCount[c];
                }
            } else {
                if(tradeMyItems[c] == actionVariable)
                    tradeCount++;
            }
        if(tradeCount + actionVariable2 < 0 && Data.itemStackable[actionVariable] == 1)
            return;
        if(tradeCount + actionVariable2 > inventoryCount(actionVariable))
            actionVariable2 = inventoryCount(actionVariable) - tradeCount;
        if(tradeCount + actionVariable2 < 0 && Data.itemStackable[actionVariable] == 0) {
            actionVariable2 = currentStack;
            actionType = 1234;
        }
        if(actionType == 1234 && Data.itemStackable[actionVariable] == 0)
            actionVariable2 = currentStack;
        if(currentStack == 0)
            return;
        boolean done = false;
        for(int c = 0; c < tradeMyItemCount; c++)
            if(tradeMyItems[c] == actionVariable && Data.itemStackable[actionVariable] == 0) {
                tradeMyItemsCount[c] += actionVariable2;
                done = true;
                break;
            }
        int count = 0;
        if(inventoryCount(actionVariable) < actionVariable2) {
            if(inventoryCount(actionVariable) - count < 1)
                return;
            else if(!((inventoryCount(actionVariable) - count) < 1))
                actionVariable2 = inventoryCount(actionVariable);
        }
        if(Data.itemStackable[actionVariable] == 1) {
            for(int c = 0; c < tradeMyItemCount; c++)
                if(tradeMyItems[c] == actionVariable)
                    count++;
            int freeSlots = 12 - tradeMyItemCount;
            if(actionVariable2 > freeSlots)
                actionVariable2 = freeSlots;
            for(int c = 0; c < actionVariable2 - 1; c++) {
                tradeMyItems[tradeMyItemCount] = actionVariable;
                tradeMyItemsCount[tradeMyItemCount] = actionVariable2;
                tradeMyItemCount++;
            }
        }
        if(!done && !((inventoryCount(actionVariable) - count) < 1)) {
            tradeMyItems[tradeMyItemCount] = actionVariable;
            tradeMyItemsCount[tradeMyItemCount] = actionVariable2;
            tradeMyItemCount++;
        }
        lastTradeDuelUpdate = System.currentTimeMillis();
        super.streamClass.addNewFrame(242);
        super.streamClass.addByte(tradeMyItemCount);
        for(int c = 0; c < tradeMyItemCount; c++) {
            super.streamClass.addShort(tradeMyItems[c]);
            super.streamClass.addInt(tradeMyItemsCount[c]);
        }
        super.streamClass.formatCurrentFrame();
        tradeOtherAccepted = false;
        tradeWeAccepted = false;
    }

    private void autoRotateCamera() {
        if((cameraAutoAngle & 1) == 1 && enginePlayerVisible(cameraAutoAngle))
            return;
        if((cameraAutoAngle & 1) == 0 && enginePlayerVisible(cameraAutoAngle)) {
            if(enginePlayerVisible(cameraAutoAngle + 1 & 7)) {
                cameraAutoAngle = cameraAutoAngle + 1 & 7;
                return;
            }
            if(enginePlayerVisible(cameraAutoAngle + 7 & 7))
                cameraAutoAngle = cameraAutoAngle + 7 & 7;
            return;
        }
        int[] ai;
        ai = new int[] { 1, -1, 2, -2, 3, -3, 4 };
        for(int i = 0; i < 7; i++) {
            if(!enginePlayerVisible(cameraAutoAngle + ai[i] + 8 & 7))
                continue;
            cameraAutoAngle = cameraAutoAngle + ai[i] + 8 & 7;
            break;
        }
        if((cameraAutoAngle & 1) == 0 && enginePlayerVisible(cameraAutoAngle)) {
            if(enginePlayerVisible(cameraAutoAngle + 1 & 7)) {
                cameraAutoAngle = cameraAutoAngle + 1 & 7;
                return;
            }
            if(enginePlayerVisible(cameraAutoAngle + 7 & 7))
                cameraAutoAngle = cameraAutoAngle + 7 & 7;
        }
    }

    @Override
    protected final void cantLogout() {
        logoutTimeout = 0;
        displayMessage("@cya@Sorry, you can't logout at the moment", 3, null);
    }

    private void checkMouseOverMenus() {
        if(mouseOverMenu == 0 && super.mouseX >= surface.menuMaxWidth - 35 && super.mouseY >= 3 && super.mouseX < surface.menuMaxWidth - 3 && super.mouseY < 35)
            mouseOverMenu = 1;
        if(mouseOverMenu == 0 && super.mouseX >= surface.menuMaxWidth - 35 - 33 && super.mouseY >= 3 && super.mouseX < surface.menuMaxWidth - 3 - 33 && super.mouseY < 35) {
            mouseOverMenu = 2;
            anInt985 = (int) (Math.random() * 13D) - 6;
            anInt986 = (int) (Math.random() * 23D) - 11;
        }
        if(mouseOverMenu == 0 && super.mouseX >= surface.menuMaxWidth - 35 - 66 && super.mouseY >= 3 && super.mouseX < surface.menuMaxWidth - 3 - 66 && super.mouseY < 35)
            mouseOverMenu = 3;
        if(mouseOverMenu == 0 && super.mouseX >= surface.menuMaxWidth - 35 - 99 && super.mouseY >= 3 && super.mouseX < surface.menuMaxWidth - 3 - 99 && super.mouseY < 35)
            mouseOverMenu = 4;
        if(mouseOverMenu == 0 && super.mouseX >= surface.menuMaxWidth - 35 - 132 && super.mouseY >= 3 && super.mouseX < surface.menuMaxWidth - 3 - 132 && super.mouseY < 35)
            mouseOverMenu = 5;
        if(mouseOverMenu == 0 && super.mouseX >= surface.menuMaxWidth - 35 - 165 && super.mouseY >= 3 && super.mouseX < surface.menuMaxWidth - 3 - 165 && super.mouseY < 35)
            mouseOverMenu = 6;
        if(mouseOverMenu != 0 && super.mouseX >= surface.menuMaxWidth - 35 && super.mouseY >= 3 && super.mouseX < surface.menuMaxWidth - 3 && super.mouseY < 26)
            mouseOverMenu = 1;
        if(mouseOverMenu != 0 && mouseOverMenu != 2 && super.mouseX >= surface.menuMaxWidth - 35 - 33 && super.mouseY >= 3 && super.mouseX < surface.menuMaxWidth - 3 - 33 && super.mouseY < 26) {
            mouseOverMenu = 2;
            anInt985 = (int) (Math.random() * 13D) - 6;
            anInt986 = (int) (Math.random() * 23D) - 11;
        }
        if(mouseOverMenu != 0 && super.mouseX >= surface.menuMaxWidth - 35 - 66 && super.mouseY >= 3 && super.mouseX < surface.menuMaxWidth - 3 - 66 && super.mouseY < 26)
            mouseOverMenu = 3;
        if(mouseOverMenu != 0 && super.mouseX >= surface.menuMaxWidth - 35 - 99 && super.mouseY >= 3 && super.mouseX < surface.menuMaxWidth - 3 - 99 && super.mouseY < 26)
            mouseOverMenu = 4;
        if(mouseOverMenu != 0 && super.mouseX >= surface.menuMaxWidth - 35 - 132 && super.mouseY >= 3 && super.mouseX < surface.menuMaxWidth - 3 - 132 && super.mouseY < 26)
            mouseOverMenu = 5;
        if(mouseOverMenu != 0 && super.mouseX >= surface.menuMaxWidth - 35 - 165 && super.mouseY >= 3 && super.mouseX < surface.menuMaxWidth - 3 - 165 && super.mouseY < 26)
            mouseOverMenu = 6;
        if(mouseOverMenu == 1 && (super.mouseX < surface.menuMaxWidth - 248 || super.mouseY > 36 + (anInt882 / 5) * 34))
            mouseOverMenu = 0;
        if(mouseOverMenu == 3 && (super.mouseX < surface.menuMaxWidth - 199 || super.mouseY > 316))
            mouseOverMenu = 0;
        if((mouseOverMenu == 2 || mouseOverMenu == 4 || mouseOverMenu == 5) && (super.mouseX < surface.menuMaxWidth - 199 || super.mouseY > 240))
            mouseOverMenu = 0;
        if(mouseOverMenu == 6 && (super.mouseX < surface.menuMaxWidth - 199 || super.mouseY > 311))
            mouseOverMenu = 0;
    }

    private void checkMouseStatus() {
        boolean hideMenu = false;
        if(hideMenu) {
            return;
        }
        if(selectedSpell >= 0 || selectedItem >= 0) {
            menuText1[menuLength] = "Cancel";
            menuText2[menuLength] = "";
            menuID[menuLength] = 4000;
            menuLength++;
        }
        for(int i = 0; i < menuLength; i++)
            menuIndexes[i] = i;
        for(boolean flag = false; !flag; ) {
            flag = true;
            for(int j = 0; j < menuLength - 1; j++) {
                int l = menuIndexes[j];
                int j1 = menuIndexes[j + 1];
                if(menuID[l] > menuID[j1]) {
                    menuIndexes[j] = j1;
                    menuIndexes[j + 1] = l;
                    flag = false;
                }
            }
        }
        if(menuLength > 20)
            menuLength = 20;
        if(menuLength > 0) {
            int k = -1;
            for(int i1 = 0; i1 < menuLength; i1++) {
                if(menuText2[menuIndexes[i1]] == null || menuText2[menuIndexes[i1]].length() <= 0)
                    continue;
                k = i1;
                break;
            }
            String s = null;
            if((selectedItem >= 0 || selectedSpell >= 0) && menuLength == 1)
                s = "Choose a target";
            else if((selectedItem >= 0 || selectedSpell >= 0) && menuLength > 1)
                s = "@whi@" + menuText1[menuIndexes[0]] + " " + menuText2[menuIndexes[0]];
            else if(k != -1)
                s = menuText2[menuIndexes[k]] + ": @whi@" + menuText1[menuIndexes[0]];
            if(menuLength == 2 && s != null)
                s = s + "@whi@ / 1 more option";
            if(menuLength > 2 && s != null)
                s = s + "@whi@ / " + (menuLength - 1) + " more options";
            if(s != null)
                surface.drawString(s, 6, 14, 1, 0xffff00);
            if(!configMouseButtons && mouseButtonClick == 1 || configMouseButtons && mouseButtonClick == 1 && menuLength == 1) {
                menuClick(menuIndexes[0]);
                mouseButtonClick = 0;
                return;
            }
            if(!configMouseButtons && mouseButtonClick == 2 || configMouseButtons && mouseButtonClick == 1) {
                menuHeight = (menuLength + 1) * 15;
                menuWidth = surface.stringWidth("Choose option", 1) + 5;
                for(int k1 = 0; k1 < menuLength; k1++) {
                    int l1 = surface.stringWidth(menuText1[k1] + " " + menuText2[k1], 1) + 5;
                    if(l1 > menuWidth)
                        menuWidth = l1;
                }
                menuX = (mouseX - menuWidth / 2);
                menuY = (mouseY - 7);
                showRightClickMenu = true;
                if(menuX < 0)
                    menuX = 5;
                if(menuY < 0)
                    menuY = 5;
                if(menuX + menuWidth > gameWidth)
                    menuX = (gameWidth - menuWidth - 5);
                if(menuY + menuHeight > gameHeight)
                    menuY = (gameHeight - menuHeight - 19);
                mouseButtonClick = 0;
            }
        }
    }

    public boolean containsOnlyNumbers(String str) {
        if(str == null || str.length() == 0)
            return false;
        for(int i = 0; i < str.length(); i++) {
            if(!Character.isDigit(str.charAt(i)))
                return false;
        }
        return true;
    }

    public void debug(String s) {
        if(userGroup == 2)
            System.out.println(s);
    }

    private void displayMessage(String message, int type, Actor actor) {
        if(type == 2 || type == 4 || type == 6) {
            for(; message.length() > 5 && message.charAt(0) == '@' && message.charAt(4) == '@'; )
                message = message.substring(5);
            int j = message.indexOf(":");
            if(j != -1) {
                String s1 = message.substring(0, j);
                long l = Utility.base37Encode(s1);
                for(int i1 = 0; i1 < super.ignoreListCount; i1++)
                    if(super.ignoreListLongs[i1] == l)
                        return;
            }
        }
        if(type == 2) {
            if(actor != null && actor.clanName != null) {
                message = "@cya@[" + actor.clanName + "]" + "@yel@" + message;
            } else
                message = "@yel@" + message;
        }
        if(type == 3 || type == 4)
            message = "@whi@" + message;
        if(type == 6)
            message = "@cya@" + message;
        if(messagesTab != 0) {
            if(type == 4 || type == 3)
                anInt952 = 200;
            if(type == 2 && messagesTab != 1)
                anInt953 = 200;
            if(type == 5 && messagesTab != 2)
                anInt954 = 200;
            if(type == 6 && messagesTab != 3)
                anInt955 = 200;
            if(type == 3)
                messagesTab = 0;
            if(type == 6 && messagesTab != 3)
                messagesTab = 0;
        }
        for(int k = 4; k > 0; k--) {
            messagesArray[k] = messagesArray[k - 1];
            messagesTimeout[k] = messagesTimeout[k - 1];
        }
        messagesArray[0] = message;
        messagesTimeout[0] = 300;
        if(type == 2)
            if(gameMenu.anIntArray187[generalChatHandle] == gameMenu.menuListTextCount[generalChatHandle] - 4)
                gameMenu.addString(generalChatHandle, message, true);
            else
                gameMenu.addString(generalChatHandle, message, false);
        if(type == 5)
            if(gameMenu.anIntArray187[questChatHandle] == gameMenu.menuListTextCount[questChatHandle] - 4)
                gameMenu.addString(questChatHandle, message, true);
            else
                gameMenu.addString(questChatHandle, message, false);
        if(type == 6) {
            if(gameMenu.anIntArray187[messagesHandleType6] == gameMenu.menuListTextCount[messagesHandleType6] - 4) {
                gameMenu.addString(messagesHandleType6, message, true);
                return;
            }
            gameMenu.addString(messagesHandleType6, message, false);
        }
    }

    private void doBankFunction(int amount) {
        super.streamClass.addNewFrame(deposit ? 152 : 224);
        super.streamClass.addShort(inputID);
        super.streamClass.addInt(amount);
        super.streamClass.formatCurrentFrame();
    }

    private void doBankFunction(int id, int amount, boolean deposit) {
        super.streamClass.addNewFrame(deposit ? 152 : 224);
        super.streamClass.addShort(id);
        super.streamClass.addInt(amount);
        super.streamClass.formatCurrentFrame();
    }

    public boolean doBuiltInCommands(String s) {
        try {
            if(s.startsWith("reset")) {
                GameUIs.reload();
            }
        } catch(Exception e) {
        }
        return false;
    }

    private void drawAbuseWindow1() {
        abuseSelectedType = 0;
        int i = 135;
        for(int j = 0; j < 6; j++) {
            if(super.mouseX > 66 && super.mouseX < 446 && super.mouseY >= i - 12 && super.mouseY < i + 3)
                abuseSelectedType = j + 1;
            i += 14;
        }
        if(mouseButtonClick != 0 && abuseSelectedType != 0) {
            mouseButtonClick = 0;
            showAbuseWindow = 2;
            super.inputText = "";
            super.enteredText = "";
            return;
        }
        i += 15;
        if(mouseButtonClick != 0) {
            mouseButtonClick = 0;
            if(super.mouseX < 56 || super.mouseY < 35 || super.mouseX > 456 || super.mouseY > 325) {
                showAbuseWindow = 0;
                return;
            }
            if(super.mouseX > 66 && super.mouseX < 446 && super.mouseY >= i - 15 && super.mouseY < i + 5) {
                showAbuseWindow = 0;
                return;
            }
        }
        surface.drawBox(56, 35, 400, 290, 0);
        surface.drawBoxEdge(56, 35, 400, 290, 0xffffff);
        i = 50;
        surface.drawStringCentered("This form is for reporting players who are breaking our rules", gameWidth / 2, i, 1, 0xffffff);
        i += 15;
        surface.drawStringCentered("Using it sends a snapshot of the last 60 secs of activity to us", gameWidth / 2, i, 1, 0xffffff);
        i += 15;
        surface.drawStringCentered("If you misuse this form, you will be banned.", gameWidth / 2, i, 1, 0xff8000);
        i += 15;
        i += 10;
        surface.drawStringCentered("First indicate which of our 12 rules is being broken. For a detailed", gameWidth / 2, i, 1, 0xffff00);
        i += 15;
        surface.drawStringCentered("explanation of each rule please read the manual on our website.", gameWidth / 2, i, 1, 0xffff00);
        i += 15;
        int k = 0xffffff;
        if(abuseSelectedType == 1) {
            surface.drawBoxEdge(66, i - 12, 380, 15, 0xffffff);
            k = 0xff8000;
        }
        surface.drawStringCentered("1: Item scamming", gameWidth / 2, i, 1, k);
        i += 14;
        if(abuseSelectedType == 2) {
            surface.drawBoxEdge(66, i - 12, 380, 15, 0xffffff);
            k = 0xff8000;
        } else {
            k = 0xffffff;
        }
        surface.drawStringCentered("2: Password scamming", gameWidth / 2, i, 1, k);
        i += 14;
        if(abuseSelectedType == 3) {
            surface.drawBoxEdge(66, i - 12, 380, 15, 0xffffff);
            k = 0xff8000;
        } else {
            k = 0xffffff;
        }
        surface.drawStringCentered("3: Bug abuse", gameWidth / 2, i, 1, k);
        i += 14;
        if(abuseSelectedType == 4) {
            surface.drawBoxEdge(66, i - 12, 380, 15, 0xffffff);
            k = 0xff8000;
        } else {
            k = 0xffffff;
        }
        surface.drawStringCentered("4: Staff impersonation", gameWidth / 2, i, 1, k);
        i += 14;
        if(abuseSelectedType == 5) {
            surface.drawBoxEdge(66, i - 12, 380, 15, 0xffffff);
            k = 0xff8000;
        } else {
            k = 0xffffff;
        }
        surface.drawStringCentered("5: Macroing", gameWidth / 2, i, 1, k);
        i += 14;
        if(abuseSelectedType == 6) {
            surface.drawBoxEdge(66, i - 12, 380, 15, 0xffffff);
            k = 0xff8000;
        } else {
            k = 0xffffff;
        }
        surface.drawStringCentered("6: Advertising / website", gameWidth / 2, i, 1, k);
        i += 14;
        if(abuseSelectedType == 7) {
            surface.drawBoxEdge(66, i - 12, 380, 15, 0xffffff);
            k = 0xff8000;
        } else {
            k = 0xffffff;
        }
        surface.drawStringCentered("Click here to cancel", gameWidth / 2, i, 1, k);
    }

    private void drawAbuseWindow2() {
        if(super.enteredText.length() > 0) {
            String s = super.enteredText.trim();
            super.inputText = "";
            super.enteredText = "";
            if(s.length() > 0) {
                long l = Utility.base37Encode(s);
                super.streamClass.addNewFrame(7);
                super.streamClass.addLong(l);
                super.streamClass.addByte(abuseSelectedType);
                super.streamClass.formatCurrentFrame();
                debug("Sent abuse report type: " + abuseSelectedType);
            }
            showAbuseWindow = 0;
            return;
        }
        surface.drawBox(56, 130, 400, 100, 0);
        surface.drawBoxEdge(56, 130, 400, 100, 0xffffff);
        int i = 160;
        surface.drawStringCentered("Now type the name of the offending player, and press enter", gameWidth / 2, i, 1, 0xffff00);
        i += 18;
        surface.drawStringCentered("Name: " + super.inputText + "*", gameWidth / 2, i, 4, 0xffffff);
        i = 222;
        int j = 0xffffff;
        if(super.mouseX > 196 && super.mouseX < 316 && super.mouseY > i - 13 && super.mouseY < i + 2) {
            j = 0xffff00;
            if(mouseButtonClick == 1) {
                mouseButtonClick = 0;
                showAbuseWindow = 0;
            }
        }
        surface.drawStringCentered("Click here to cancel", gameWidth / 2, i, 1, j);
        if(mouseButtonClick == 1 && (super.mouseX < 56 || super.mouseX > 456 || super.mouseY < 130 || super.mouseY > 230)) {
            mouseButtonClick = 0;
            showAbuseWindow = 0;
        }
    }

    private void drawCharacterLookScreen() {
        characterLooksScreenChanged = characterDesignMenu.updateActions(super.mouseX, super.mouseY, super.lastMouseDownButton, super.mouseDownButton);
        if(characterDesignMenu.hasActivated(characterDesignHeadButton1))
            do
                characterHeadType = ((characterHeadType - 1) + Data.integerAnimationCount) % Data.integerAnimationCount;
            while((Data.animationGenderModels[characterHeadType] & 3) != 1 || (Data.animationGenderModels[characterHeadType] & 4 * characterHeadGender) == 0);
        if(characterDesignMenu.hasActivated(characterDesignHeadButton2))
            do
                characterHeadType = (characterHeadType + 1) % Data.integerAnimationCount;
            while((Data.animationGenderModels[characterHeadType] & 3) != 1 || (Data.animationGenderModels[characterHeadType] & 4 * characterHeadGender) == 0);
        if(characterDesignMenu.hasActivated(characterDesignHairColorButton1))
            characterHairColor = ((characterHairColor - 1) + HAIR_COLORS.length) % HAIR_COLORS.length;
        if(characterDesignMenu.hasActivated(characterDesignHairColorButton2))
            characterHairColor = (characterHairColor + 1) % HAIR_COLORS.length;
        if(characterDesignMenu.hasActivated(characterDesignGenderButton1) || characterDesignMenu.hasActivated(characterDesignGenderButton2)) {
            for(characterHeadGender = 3 - characterHeadGender; (Data.animationGenderModels[characterHeadType] & 3) != 1 || (Data.animationGenderModels[characterHeadType] & 4 * characterHeadGender) == 0; )
                characterHeadType = (characterHeadType + 1) % Data.integerAnimationCount;
            for(; (Data.animationGenderModels[characterBodyGender] & 3) != 2 || (Data.animationGenderModels[characterBodyGender] & 4 * characterHeadGender) == 0; )
                characterBodyGender = (characterBodyGender + 1) % Data.integerAnimationCount;
        }
        if(characterDesignMenu.hasActivated(characterDesignTopColorButton1))
            characterTopColor = ((characterTopColor - 1) + CLOTHES_COLORS.length) % CLOTHES_COLORS.length;
        if(characterDesignMenu.hasActivated(characterDesignTopColorButton2))
            characterTopColor = (characterTopColor + 1) % CLOTHES_COLORS.length;
        if(characterDesignMenu.hasActivated(characterDesignSkinColorButton1))
            characterSkinColor = ((characterSkinColor - 1) + SKIN_COLORS.length) % SKIN_COLORS.length;
        if(characterDesignMenu.hasActivated(characterDesignSkinColorButton2))
            characterSkinColor = (characterSkinColor + 1) % SKIN_COLORS.length;
        if(characterDesignMenu.hasActivated(characterDesignBottomColorButton1))
            characterBottomColor = ((characterBottomColor - 1) + CLOTHES_COLORS.length) % CLOTHES_COLORS.length;
        if(characterDesignMenu.hasActivated(characterDesignBottomColorButton2))
            characterBottomColor = (characterBottomColor + 1) % CLOTHES_COLORS.length;
        if(characterLooksScreenChanged) {
            debug("characterHeadGender = " + characterHeadGender);
            debug("characterHeadType = " + characterHeadType);
            debug("characterBodyGender = " + characterBodyGender);
            debug("character2Color = " + character2Color);
            debug("characterHairColor = " + characterHairColor);
            debug("characterTopColor = " + characterTopColor);
            debug("characterBottomColor = " + characterBottomColor);
            debug("characterSkinColor = " + characterSkinColor);
            System.out.println();
            characterLooksScreenChanged = false;
        }
        if(characterDesignMenu.hasActivated(characterDesignAcceptButton)) {
            super.streamClass.addNewFrame(81);
            super.streamClass.addByte(characterHeadGender);
            super.streamClass.addByte(characterHeadType);
            super.streamClass.addByte(characterBodyGender);
            super.streamClass.addByte(character2Color);
            super.streamClass.addByte(characterHairColor);
            super.streamClass.addByte(characterTopColor);
            super.streamClass.addByte(characterBottomColor);
            super.streamClass.addByte(characterSkinColor);
            super.streamClass.formatCurrentFrame();
            /*
             * System.out.println("characterHeadGender = " +
             * characterHeadGender); System.out.println("characterHeadType = " +
             * characterHeadType); System.out.println("characterBodyGender = " +
             * characterBodyGender); System.out.println("character2Color = " +
             * character2Color); System.out.println("characterHairColor = "
             * +characterHairColor); System.out.println("characterTopColor = " +
             * characterTopColor); System.out.println("characterBottomColor = "
             * + characterBottomColor);
             * System.out.println("characterSkinColor = " + characterSkinColor);
             */
            surface.blackScreen();
            showCharacterLookScreen = false;
        }
    }

    private void drawChatMessageTabs() {
        surface.drawSprite(0, windowHeight, SPRITE_MEDIA + 22);
        surface.drawSprite(512, windowHeight, SPRITE_MEDIA + 22);
        surface.drawSprite(windowWidth / 2 - 256, windowHeight - 4, SPRITE_MEDIA + 23);
        int i = Surface.convertRGBToLong(200, 200, 255);
        if(messagesTab == 0)
            i = Surface.convertRGBToLong(255, 200, 50);
        if(anInt952 % 30 > 15)
            i = Surface.convertRGBToLong(255, 50, 50);
        surface.drawStringCentered("All messages", windowWidth / 2 - 202, windowHeight + 6, 0, i);
        i = Surface.convertRGBToLong(200, 200, 255);
        if(messagesTab == 1)
            i = Surface.convertRGBToLong(255, 200, 50);
        if(anInt953 % 30 > 15)
            i = Surface.convertRGBToLong(255, 50, 50);
        surface.drawStringCentered("Chat history", windowWidth / 2 - 101, windowHeight + 6, 0, i);
        i = Surface.convertRGBToLong(200, 200, 255);
        if(messagesTab == 2)
            i = Surface.convertRGBToLong(255, 200, 50);
        if(anInt954 % 30 > 15)
            i = Surface.convertRGBToLong(255, 50, 50);
        surface.drawStringCentered("Server history", windowWidth / 2 - 1, windowHeight + 6, 0, i);
        i = Surface.convertRGBToLong(200, 200, 255);
        if(messagesTab == 3)
            i = Surface.convertRGBToLong(255, 200, 50);
        if(anInt955 % 30 > 15)
            i = Surface.convertRGBToLong(255, 50, 50);
        surface.drawStringCentered("Private history", windowWidth / 2 + 99, windowHeight + 6, 0, i);
        surface.drawStringCentered("Report abuse", windowWidth / 2 + 201, windowHeight + 6, 0, 0xffffff);
    }

    private void drawCombatStyleWindow() {
        byte byte0 = 7;
        byte byte1 = 6;
        int c = 140;
        if(mouseButtonClick != 0) {
            for(int i = 0; i < 5; i++) {
                if(i <= 0 || super.mouseX <= byte0 || super.mouseX >= byte0 + c || super.mouseY <= byte1 + i * 20 || super.mouseY >= byte1 + i * 20 + 20)
                    continue;
                combatStyle = i - 1;
                mouseButtonClick = 0;
                super.streamClass.addNewFrame(88);
                super.streamClass.addByte(combatStyle);
                super.streamClass.formatCurrentFrame();
                break;
            }
        }
        for(int j = 0; j < 5; j++) {
            if(j == combatStyle + 1)
                surface.drawBoxAlpha(byte0, byte1 + j * 20, c, 20, Surface.convertRGBToLong(255, 0, 0), 128);
            else
                surface.drawBoxAlpha(byte0, byte1 + j * 20, c, 20, Surface.convertRGBToLong(190, 190, 190), 128);
            surface.drawLineX(byte0, byte1 + j * 20, c, 0);
            surface.drawLineX(byte0, byte1 + j * 20 + 20, c, 0);
        }
        surface.drawStringCentered("Combat style", byte0 + c / 2, byte1 + 16, 3, 0xffffff);
        surface.drawStringCentered("Controlled (+1 all)", byte0 + c / 2, byte1 + 36, 3, 0);
        surface.drawStringCentered("Aggressive (+3 str)", byte0 + c / 2, byte1 + 56, 3, 0);
        surface.drawStringCentered("Accurate   (+3 att)", byte0 + c / 2, byte1 + 76, 3, 0);
        surface.drawStringCentered("Defensive  (+3 def)", byte0 + c / 2, byte1 + 96, 3, 0);
    }

    private void drawDuelConfirmWindow() {
        int byte0 = gameWidth / 2 - 234;
        int byte1 = gameHeight - (gameHeight / 2 + 131);
        surface.drawBox(byte0, byte1, 468, 16, 192);
        int i = 0x989898;
        surface.drawBoxAlpha(byte0, byte1 + 16, 468, 246, i, 160);
        surface.drawStringCentered("Please confirm your duel with @yel@" + Utility.base37Decode(duelOpponentNameLong), byte0 + 234, byte1 + 12, 1, 0xffffff);
        surface.drawStringCentered("Your stake:", byte0 + 117, byte1 + 30, 1, 0xffff00);
        for(int j = 0; j < duelConfirmMyItemCount; j++) {
            String s = Data.itemName[duelConfirmMyItems[j]];
            if(Data.itemStackable[duelConfirmMyItems[j]] == 0)
                s = s + " x " + itemAmountToString(duelConfirmMyItemsCount[j]);
            surface.drawStringCentered(s, byte0 + 117, byte1 + 42 + j * 12, 1, 0xffffff);
        }
        if(duelConfirmMyItemCount == 0)
            surface.drawStringCentered("Nothing!", byte0 + 117, byte1 + 42, 1, 0xffffff);
        surface.drawStringCentered("Your opponent's stake:", byte0 + 351, byte1 + 30, 1, 0xffff00);
        for(int k = 0; k < duelConfirmOpponentItemCount; k++) {
            String s1 = Data.itemName[duelConfirmOpponentItems[k]];
            if(Data.itemStackable[duelConfirmOpponentItems[k]] == 0)
                s1 = s1 + " x " + itemAmountToString(duelConfirmOpponentItemsCount[k]);
            surface.drawStringCentered(s1, byte0 + 351, byte1 + 42 + k * 12, 1, 0xffffff);
        }
        if(duelConfirmOpponentItemCount == 0)
            surface.drawStringCentered("Nothing!", byte0 + 351, byte1 + 42, 1, 0xffffff);
        if(duelCantRetreat == 0)
            surface.drawStringCentered("You can retreat from this duel", byte0 + 234, byte1 + 180, 1, 65280);
        else
            surface.drawStringCentered("No retreat is possible!", byte0 + 234, byte1 + 180, 1, 0xff0000);
        if(duelUseMagic == 0)
            surface.drawStringCentered("Magic may be used", byte0 + 234, byte1 + 192, 1, 65280);
        else
            surface.drawStringCentered("Magic cannot be used", byte0 + 234, byte1 + 192, 1, 0xff0000);
        if(duelUsePrayer == 0)
            surface.drawStringCentered("Prayer may be used", byte0 + 234, byte1 + 204, 1, 65280);
        else
            surface.drawStringCentered("Prayer cannot be used", byte0 + 234, byte1 + 204, 1, 0xff0000);
        if(duelUseWeapons == 0)
            surface.drawStringCentered("Weapons may be used", byte0 + 234, byte1 + 216, 1, 65280);
        else
            surface.drawStringCentered("Weapons cannot be used", byte0 + 234, byte1 + 216, 1, 0xff0000);
        surface.drawStringCentered("If you are sure click 'Accept' to begin the duel", byte0 + 234, byte1 + 230, 1, 0xffffff);
        if(!duelWeAccept) {
            surface.drawSprite((byte0 + 118) - 35, byte1 + 238, SPRITE_MEDIA + 25);
            surface.drawSprite((byte0 + 352) - 35, byte1 + 238, SPRITE_MEDIA + 26);
        } else {
            surface.drawStringCentered("Waiting for other player...", byte0 + 234, byte1 + 250, 1, 0xffff00);
        }
        if(mouseButtonClick == 1) {
            if(super.mouseX < byte0 || super.mouseY < byte1 || super.mouseX > byte0 + 468 || super.mouseY > byte1 + 262) {
                showDuelConfirmWindow = false;
                super.streamClass.addNewFrame(15);
                super.streamClass.formatCurrentFrame();
            }
            if(super.mouseX >= (byte0 + 118) - 35 && super.mouseX <= byte0 + 118 + 70 && super.mouseY >= byte1 + 238 && super.mouseY <= byte1 + 238 + 21) {
                duelWeAccept = true;
                super.streamClass.addNewFrame(33);
                super.streamClass.formatCurrentFrame();
            }
            if(super.mouseX >= (byte0 + 352) - 35 && super.mouseX <= byte0 + 353 + 70 && super.mouseY >= byte1 + 238 && super.mouseY <= byte1 + 238 + 21) {
                showDuelConfirmWindow = false;
                super.streamClass.addNewFrame(15);
                super.streamClass.formatCurrentFrame();
            }
            mouseButtonClick = 0;
        }
    }

    private void drawDuelWindow() {
        if(clickScreenSend) {
            mouseButtonClick = 4;
            clickScreenSend = false;
        }
        if(System.currentTimeMillis() - lastTradeDuelUpdate > 50) {
            if(mouseButtonClick != 0 && itemIncrement == 0)
                itemIncrement = 1;
            allowSendCommand = !(inputBoxType > 3 && inputBoxType < 10);
            if(allowSendCommand)
                if(itemIncrement > 0) {
                    int i = mouseX - (gameWidth - (gameWidth / 2 + 234));
                    int j = mouseY - (gameHeight / 2 - 139);
                    if((i >= 0) && (j >= 0) && (i < 468) && (j < 262)) {
                        if((i > 216) && (j > 30) && (i < 462) && (j < 235)) {
                            int k = (i - 217) / 49 + (j - 31) / 34 * 5;
                            if(k >= 0 && k < inventoryCount) {
                                boolean flag1 = false;
                                int l1 = 0;
                                int k2 = inventoryItems[k];
                                if(mouseButtonClick != 2 && !rightClickOptions && mouseButtonClick != 4) {
                                    for(int k3 = 0; k3 < duelMyItemCount; k3++)
                                        if(duelMyItems[k3] == k2)
                                            if(Data.itemStackable[k2] == 0) {
                                                for(int i4 = 0; i4 < itemIncrement; i4++) {
                                                    if(duelMyItemsCount[k3] < inventoryItemsCount[k])
                                                        duelMyItemsCount[k3]++;
                                                    flag1 = true;
                                                }
                                            } else
                                                l1++;
                                    if(inventoryCount(k2) <= l1)
                                        flag1 = true;
                                    if(!flag1 && duelMyItemCount < 8) {
                                        duelMyItems[duelMyItemCount] = k2;
                                        duelMyItemsCount[duelMyItemCount] = 1;
                                        duelMyItemCount++;
                                        flag1 = true;
                                    }
                                }
                                if(mouseButtonClick != 2 && !rightClickOptions && mouseButtonClick != 4) {
                                    if(flag1) {
                                        lastTradeDuelUpdate = System.currentTimeMillis();
                                        super.streamClass.addNewFrame(gameHeight / 2);
                                        super.streamClass.addByte(duelMyItemCount);
                                        for(int duelItem = 0; duelItem < duelMyItemCount; duelItem++) {
                                            super.streamClass.addShort(duelMyItems[duelItem]);
                                            super.streamClass.addInt(duelMyItemsCount[duelItem]);
                                        }
                                        super.streamClass.formatCurrentFrame();
                                        duelOpponentAccepted = false;
                                        duelMyAccepted = false;
                                    }
                                } else if(mouseButtonClick == 2) {
                                    if(rightClickOptions) {
                                        tradeWindowX = -100;
                                        tradeWindowY = -100;
                                        mouseButtonClick = 0;
                                        rightClickOptions = false;
                                        valueSet = false;
                                    } else if(!rightClickOptions) {
                                        tradeWindowX = super.mouseX;
                                        tradeWindowY = super.mouseY;
                                        for(int jx = 0; jx < menuLength; jx++) {
                                            menuText1[jx] = null;
                                            menuText2[jx] = null;
                                            menuActionVariable[jx] = -1;
                                            menuActionVariable2[jx] = -1;
                                            menuID[jx] = -1;
                                        }
                                        String name = Data.itemName[k2];
                                        menuLength = 0;
                                        menuText1[menuLength] = "Stake 1 @lre@";
                                        menuText2[menuLength] = name;
                                        menuID[menuLength] = 882;
                                        menuActionVariable[menuLength] = k2;
                                        menuActionVariable2[menuLength] = 1;
                                        menuLength++;
                                        menuText1[menuLength] = "Stake 5 @lre@";
                                        menuText2[menuLength] = name;
                                        menuID[menuLength] = 882;
                                        menuActionVariable[menuLength] = k2;
                                        menuActionVariable2[menuLength] = 5;
                                        menuLength++;
                                        menuText1[menuLength] = "Stake 10 @lre@";
                                        menuText2[menuLength] = name;
                                        menuID[menuLength] = 882;
                                        menuActionVariable[menuLength] = k2;
                                        menuActionVariable2[menuLength] = 10;
                                        menuLength++;
                                        menuText1[menuLength] = "Stake All @lre@";
                                        menuText2[menuLength] = name;
                                        menuID[menuLength] = 882;
                                        menuActionVariable[menuLength] = k2;
                                        menuActionVariable2[menuLength] = inventoryCount(k2);
                                        menuActionType[menuLength] = 1234;
                                        menuLength++;
                                        menuText1[menuLength] = "Stake X @lre@";
                                        menuText2[menuLength] = name;
                                        menuID[menuLength] = 890;
                                        menuActionVariable[menuLength] = k2;
                                        menuLength++;
                                        rightClickOptions = true;
                                    }
                                }
                            }
                        }
                        if(i > 8 && j > 30 && i < 205 && j < 129) {
                            int l = (i - 9) / 49 + ((j - 31) / 34) * 4;
                            if(l >= 0 && l < duelMyItemCount) {
                                int j1 = duelMyItems[l];
                                if(mouseButtonClick != 2 && !rightClickOptions) {
                                    for(int i2 = 0; i2 < itemIncrement; i2++) {
                                        if(Data.itemStackable[j1] == 0 && duelMyItemsCount[l] > 1) {
                                            duelMyItemsCount[l]--;
                                            continue;
                                        }
                                        duelMyItemCount--;
                                        mouseDownTime = 0;
                                        for(int l2 = l; l2 < duelMyItemCount; l2++) {
                                            duelMyItems[l2] = duelMyItems[l2 + 1];
                                            duelMyItemsCount[l2] = duelMyItemsCount[l2 + 1];
                                        }
                                        break;
                                    }
                                    if(mouseButtonClick != 2 && !rightClickOptions && mouseButtonClick != 4) {
                                        lastTradeDuelUpdate = System.currentTimeMillis();
                                        super.streamClass.addNewFrame(gameHeight / 2);
                                        super.streamClass.addByte(duelMyItemCount);
                                        for(int i3 = 0; i3 < duelMyItemCount; i3++) {
                                            super.streamClass.addShort(duelMyItems[i3]);
                                            super.streamClass.addInt(duelMyItemsCount[i3]);
                                        }
                                        super.streamClass.formatCurrentFrame();
                                        duelOpponentAccepted = false;
                                        duelMyAccepted = false;
                                    }
                                }
                                if(mouseButtonClick == 2) {
                                    if(rightClickOptions) {
                                        tradeWindowX = -100;
                                        tradeWindowY = -100;
                                        mouseButtonClick = 0;
                                        rightClickOptions = false;
                                        valueSet = false;
                                    } else if(!rightClickOptions) {
                                        tradeWindowX = super.mouseX;
                                        tradeWindowY = super.mouseY;
                                        for(int jx = 0; jx < menuLength; jx++) {
                                            menuText1[jx] = null;
                                            menuText2[jx] = null;
                                            menuActionVariable[jx] = -1;
                                            menuActionVariable2[jx] = -1;
                                            menuID[jx] = -1;
                                        }
                                        String name = Data.itemName[j1];
                                        menuLength = 0;
                                        menuText1[menuLength] = "Remove 1 @lre@";
                                        menuText2[menuLength] = name;
                                        menuID[menuLength] = 883;
                                        menuActionVariable[menuLength] = j1;
                                        menuActionVariable2[menuLength] = 1;
                                        menuLength++;
                                        menuText1[menuLength] = "Remove 5 @lre@";
                                        menuText2[menuLength] = name;
                                        menuID[menuLength] = 883;
                                        menuActionVariable[menuLength] = j1;
                                        menuActionVariable2[menuLength] = 5;
                                        menuLength++;
                                        menuText1[menuLength] = "Remove 10 @lre@";
                                        menuText2[menuLength] = name;
                                        menuID[menuLength] = 883;
                                        menuActionVariable[menuLength] = j1;
                                        menuActionVariable2[menuLength] = 10;
                                        menuLength++;
                                        menuText1[menuLength] = "Remove All @lre@";
                                        menuText2[menuLength] = name;
                                        menuID[menuLength] = 883;
                                        menuActionVariable[menuLength] = j1;
                                        menuActionVariable2[menuLength] = inventoryCount(j1);
                                        menuActionType[menuLength] = 1234;
                                        menuLength++;
                                        menuText1[menuLength] = "Remove X @lre@";
                                        menuText2[menuLength] = name;
                                        menuID[menuLength] = 889;
                                        menuActionVariable[menuLength] = j1;
                                        menuLength++;
                                        rightClickOptions = true;
                                    }
                                }
                            }
                        }
                        boolean flag = false;
                        if(i >= 93 && j >= 221 && i <= 104 && j <= 232) {
                            duelNoRetreating = !duelNoRetreating;
                            flag = true;
                        }
                        if(i >= 93 && j >= 240 && i <= 104 && j <= 251) {
                            duelNoMagic = !duelNoMagic;
                            flag = true;
                        }
                        if(i >= 191 && j >= 221 && i <= 202 && j <= 232) {
                            duelNoPrayer = !duelNoPrayer;
                            flag = true;
                        }
                        if(i >= 191 && j >= 240 && i <= 202 && j <= 251) {
                            duelNoWeapons = !duelNoWeapons;
                            flag = true;
                        }
                        if(flag) {
                            super.streamClass.addNewFrame(255);
                            super.streamClass.addByte(duelNoRetreating ? 1 : 0);
                            super.streamClass.addByte(duelNoMagic ? 1 : 0);
                            super.streamClass.addByte(duelNoPrayer ? 1 : 0);
                            super.streamClass.addByte(duelNoWeapons ? 1 : 0);
                            super.streamClass.formatCurrentFrame();
                            duelOpponentAccepted = false;
                            duelMyAccepted = false;
                        }
                        if(i >= 217 && j >= 238 && i <= 286 && j <= 259 && !rightClickOptions) {
                            lastTradeDuelUpdate = System.currentTimeMillis();
                            duelMyAccepted = true;
                            super.streamClass.addNewFrame(90);
                            super.streamClass.formatCurrentFrame();
                        }
                        if(i >= 394 && j >= 238 && i < 463 && j < 259 && !rightClickOptions) {
                            showDuelWindow = false;
                            super.streamClass.addNewFrame(15);
                            super.streamClass.formatCurrentFrame();
                        }
                    } else if(mouseButtonClick != 0 && !rightClickOptions && mouseButtonClick != 4) {
                        showDuelWindow = false;
                        super.streamClass.addNewFrame(15);
                        super.streamClass.formatCurrentFrame();
                    }
                    if(mouseButtonClick == 1 && rightClickOptions) {
                        for(int ix = 0; ix < menuLength; ix++) {
                            int k = tradeWindowX + 2;
                            int i1 = tradeWindowY + 11 + (ix + 1) * 15;
                            if(super.mouseX <= k - 2 || super.mouseY <= i1 - 12 || super.mouseY >= i1 + 4 || super.mouseX >= (k - 3) + menuWidth)
                                continue;
                            menuClick(ix);
                        }
                        tradeWindowX = -100;
                        tradeWindowY = -100;
                        mouseButtonClick = 0;
                        rightClickOptions = false;
                        valueSet = false;
                    }
                    mouseButtonClick = 0;
                    itemIncrement = 0;
                }
        }
        if(!showDuelWindow)
            return;
        int byte0 = windowWidth / 2 - 234;
        int byte1 = windowHeight / 2 - 140;
        surface.drawBox(byte0, byte1, 468, 12, 0xc90b1d);
        int i1 = 0x989898;
        surface.drawBoxAlpha(byte0, byte1 + 12, 468, 18, i1, 160);
        surface.drawBoxAlpha(byte0, byte1 + 30, 8, 248, i1, 160);
        surface.drawBoxAlpha(byte0 + 205, byte1 + 30, 11, 248, i1, 160);
        surface.drawBoxAlpha(byte0 + 462, byte1 + 30, 6, 248, i1, 160);
        surface.drawBoxAlpha(byte0 + 8, byte1 + 99, 197, 24, i1, 160);
        surface.drawBoxAlpha(byte0 + 8, byte1 + 192, 197, 23, i1, 160);
        surface.drawBoxAlpha(byte0 + 8, byte1 + 258, 197, 20, i1, 160);
        surface.drawBoxAlpha(byte0 + 216, byte1 + 235, 246, 43, i1, 160);
        int k1 = 0xd0d0d0;
        surface.drawBoxAlpha(byte0 + 8, byte1 + 30, 197, 69, k1, 160);
        surface.drawBoxAlpha(byte0 + 8, byte1 + 123, 197, 69, k1, 160);
        surface.drawBoxAlpha(byte0 + 8, byte1 + 215, 197, 43, k1, 160);
        surface.drawBoxAlpha(byte0 + 216, byte1 + 30, 246, 205, k1, 160);
        for(int j2 = 0; j2 < 3; j2++)
            surface.drawLineX(byte0 + 8, byte1 + 30 + j2 * 34, 197, 0);
        for(int j3 = 0; j3 < 3; j3++)
            surface.drawLineX(byte0 + 8, byte1 + 123 + j3 * 34, 197, 0);
        for(int l3 = 0; l3 < 7; l3++)
            surface.drawLineX(byte0 + 216, byte1 + 30 + l3 * 34, 246, 0);
        for(int k4 = 0; k4 < 6; k4++) {
            if(k4 < 5)
                surface.drawLineY(byte0 + 8 + k4 * 49, byte1 + 30, 69, 0);
            if(k4 < 5)
                surface.drawLineY(byte0 + 8 + k4 * 49, byte1 + 123, 69, 0);
            surface.drawLineY(byte0 + 216 + k4 * 49, byte1 + 30, 205, 0);
        }
        surface.drawLineX(byte0 + 8, byte1 + 215, 197, 0);
        surface.drawLineX(byte0 + 8, byte1 + 257, 197, 0);
        surface.drawLineY(byte0 + 8, byte1 + 215, 43, 0);
        surface.drawLineY(byte0 + 204, byte1 + 215, 43, 0);
        surface.drawString("Preparing to duel with: " + duelOpponentName, byte0 + 1, byte1 + 10, 1, 0xffffff);
        surface.drawString("Your Stake", byte0 + 9, byte1 + 27, 4, 0xffffff);
        surface.drawString("Opponent's Stake", byte0 + 9, byte1 + 120, 4, 0xffffff);
        surface.drawString("Duel Options", byte0 + 9, byte1 + 212, 4, 0xffffff);
        surface.drawString("Your Inventory", byte0 + 216, byte1 + 27, 4, 0xffffff);
        surface.drawString("No retreating", byte0 + 8 + 1, byte1 + 215 + 16, 3, 0xffff00);
        surface.drawString("No magic", byte0 + 8 + 1, byte1 + 215 + 35, 3, 0xffff00);
        surface.drawString("No prayer", byte0 + 8 + 102, byte1 + 215 + 16, 3, 0xffff00);
        surface.drawString("No weapons", byte0 + 8 + 102, byte1 + 215 + 35, 3, 0xffff00);
        surface.drawBoxEdge(byte0 + 93, byte1 + 215 + 6, 11, 11, 0xffff00);
        if(duelNoRetreating)
            surface.drawBox(byte0 + 95, byte1 + 215 + 8, 7, 7, 0xffff00);
        surface.drawBoxEdge(byte0 + 93, byte1 + 215 + 25, 11, 11, 0xffff00);
        if(duelNoMagic)
            surface.drawBox(byte0 + 95, byte1 + 215 + 27, 7, 7, 0xffff00);
        surface.drawBoxEdge(byte0 + 191, byte1 + 215 + 6, 11, 11, 0xffff00);
        if(duelNoPrayer)
            surface.drawBox(byte0 + 193, byte1 + 215 + 8, 7, 7, 0xffff00);
        surface.drawBoxEdge(byte0 + 191, byte1 + 215 + 25, 11, 11, 0xffff00);
        if(duelNoWeapons)
            surface.drawBox(byte0 + 193, byte1 + 215 + 27, 7, 7, 0xffff00);
        if(!duelMyAccepted)
            surface.drawSprite(byte0 + 217, byte1 + 238, SPRITE_MEDIA + 25);
        surface.drawSprite(byte0 + 394, byte1 + 238, SPRITE_MEDIA + 26);
        if(duelOpponentAccepted) {
            surface.drawStringCentered("Other player", byte0 + 341, byte1 + 246, 1, 0xffffff);
            surface.drawStringCentered("has accepted", byte0 + 341, byte1 + 246 + 10, 1, 0xffffff);
        }
        if(duelMyAccepted) {
            surface.drawStringCentered("Waiting for", byte0 + 217 + 35, byte1 + 246, 1, 0xffffff);
            surface.drawStringCentered("other player", byte0 + 217 + 35, byte1 + 246 + 10, 1, 0xffffff);
        }
        for(int l4 = 0; l4 < inventoryCount; l4++) {
            int i5 = 217 + byte0 + (l4 % 5) * 49;
            int k5 = 31 + byte1 + (l4 / 5) * 34;
            int mask = inventoryItems[l4] == 183 ? ourPlayer.cape : Data.itemPictureMask[inventoryItems[l4]];
            surface.spriteClip4(i5, k5, 48, 32, SPRITE_ITEM + Data.itemInventoryPicture[inventoryItems[l4]], mask, 0, 0, false);
            if(Data.itemStackable[inventoryItems[l4]] == 0)
                surface.drawString(insertCommas(String.valueOf(inventoryItemsCount[l4])), i5 + 1, k5 + 10, 1, 0xffff00);
        }
        for(int j5 = 0; j5 < duelMyItemCount; j5++) {
            int l5 = 9 + byte0 + (j5 % 4) * 49;
            int j6 = 31 + byte1 + (j5 / 4) * 34;
            int mask = duelMyItems[j5] == 183 ? ourPlayer.cape : Data.itemPictureMask[duelMyItems[j5]];
            surface.spriteClip4(l5, j6, 48, 32, SPRITE_ITEM + Data.itemInventoryPicture[duelMyItems[j5]], mask, 0, 0, false);
            if(Data.itemStackable[duelMyItems[j5]] == 0)
                surface.drawString(insertCommas(String.valueOf(duelMyItemsCount[j5])), l5 + 1, j6 + 10, 1, 0xffff00);
            if(super.mouseX > l5 && super.mouseX < l5 + 48 && super.mouseY > j6 && super.mouseY < j6 + 32)
                surface.drawString(Data.itemName[duelMyItems[j5]] + ": @whi@" + Data.itemDescription[duelMyItems[j5]], byte0 + 8, byte1 + 273, 1, 0xffff00);
        }
        for(int i6 = 0; i6 < duelOpponentItemCount; i6++) {
            int k6 = 9 + byte0 + (i6 % 4) * 49;
            int l6 = 124 + byte1 + (i6 / 4) * 34;
            surface.spriteClip4(k6, l6, 48, 32, SPRITE_ITEM + Data.itemInventoryPicture[duelOpponentItems[i6]], Data.itemPictureMask[duelOpponentItems[i6]], 0, 0, false);
            if(Data.itemStackable[duelOpponentItems[i6]] == 0)
                surface.drawString(insertCommas(String.valueOf(duelOpponentItemsCount[i6])), k6 + 1, l6 + 10, 1, 0xffff00);
            if(super.mouseX > k6 && super.mouseX < k6 + 48 && super.mouseY > l6 && super.mouseY < l6 + 32)
                surface.drawString(Data.itemName[duelOpponentItems[i6]] + ": @whi@" + Data.itemDescription[duelOpponentItems[i6]], byte0 + 8, byte1 + 273, 1, 0xffff00);
        }
    }

    private void drawFriendsWindow(boolean flag) {
        int i = surface.menuMaxWidth - 199;
        int j = 36;
        surface.drawSprite(i - 49, 3, SPRITE_MEDIA + 5);
        char c = '\304';
        char c1 = '\266';
        int l;
        int k = l = Surface.convertRGBToLong(160, 160, 160);
        if(anInt981 == 0)
            k = Surface.convertRGBToLong(220, 220, 220);
        else
            l = Surface.convertRGBToLong(220, 220, 220);
        surface.drawBoxAlpha(i, j, c / 2, 24, k, 128);
        surface.drawBoxAlpha(i + c / 2, j, c / 2, 24, l, 128);
        surface.drawBoxAlpha(i, j + 24, c, c1 - 24, Surface.convertRGBToLong(220, 220, 220), 128);
        surface.drawLineX(i, j + 24, c, 0);
        surface.drawLineY(i + c / 2, j, 24, 0);
        surface.drawLineX(i, (j + c1) - 16, c, 0);
        surface.drawStringCentered("Friends", i + c / 4, j + 16, 4, 0);
        surface.drawStringCentered("Ignore", i + c / 4 + c / 2, j + 16, 4, 0);
        friendsMenu.resetListTextCount(friendsMenuHandle);
        String injector = "~" + (gameWidth - 73) + "~";
        if(anInt981 == 0) {
            for(int i1 = 0; i1 < super.friendsCount; i1++) {
                String s;
                if(super.friendsListOnlineStatus[i1] == 99)
                    s = "@gre@";
                else if(super.friendsListOnlineStatus[i1] > 0)
                    s = "@yel@";
                else
                    s = "@red@";
                friendsMenu.drawMenuListText(friendsMenuHandle, i1, s + Utility.base37Decode(friendsListLongs[i1]) + injector + "@whi@Remove                      WWWWWWWWWW");
            }
        }
        if(anInt981 == 1) {
            for(int j1 = 0; j1 < super.ignoreListCount; j1++)
                friendsMenu.drawMenuListText(friendsMenuHandle, j1, "@yel@" + Utility.base37Decode(super.ignoreListLongs[j1]) + injector + "@whi@Remove         WWWWWWWWWW");
        }
        friendsMenu.drawMenu();
        if(anInt981 == 0) {
            int k1 = friendsMenu.selectedListIndex(friendsMenuHandle);
            if((k1 >= 0) && (mouseX < gameWidth - 20)) {
                if(mouseX > gameWidth - 75)
                    surface.drawStringCentered("Click to remove " + Utility.base37Decode(super.friendsListLongs[k1]), i + c / 2, j + 35, 1, 0xffffff);
                else if(super.friendsListOnlineStatus[k1] == 99)
                    surface.drawStringCentered("Click to message " + Utility.base37Decode(super.friendsListLongs[k1]), i + c / 2, j + 35, 1, 0xffffff);
                else if(super.friendsListOnlineStatus[k1] > 0)
                    surface.drawStringCentered(Utility.base37Decode(super.friendsListLongs[k1]) + " is on world " + super.friendsListOnlineStatus[k1], i + c / 2, j + 35, 1, 0xffffff);
                else
                    surface.drawStringCentered(Utility.base37Decode(super.friendsListLongs[k1]) + " is offline", i + c / 2, j + 35, 1, 0xffffff);
            } else {
                surface.drawStringCentered("Click a name to send a message", i + c / 2, j + 35, 1, 0xffffff);
            }
            int k2;
            if(super.mouseX > i && super.mouseX < i + c && super.mouseY > (j + c1) - 16 && super.mouseY < j + c1)
                k2 = 0xffff00;
            else
                k2 = 0xffffff;
            surface.drawStringCentered("Click here to add a friend", i + c / 2, (j + c1) - 3, 1, k2);
        }
        if(anInt981 == 1) {
            int l1 = friendsMenu.selectedListIndex(friendsMenuHandle);
            if((l1 >= 0) && (mouseX < gameWidth - 20)) {
                if(mouseX > gameWidth - 75)
                    surface.drawStringCentered("Click to remove " + Utility.base37Decode(super.ignoreListLongs[l1]), i + c / 2, j + 35, 1, 0xffffff);
            } else {
                surface.drawStringCentered("Blocking messages from:", i + c / 2, j + 35, 1, 0xffffff);
            }
            int l2;
            if(super.mouseX > i && super.mouseX < i + c && super.mouseY > (j + c1) - 16 && super.mouseY < j + c1)
                l2 = 0xffff00;
            else
                l2 = 0xffffff;
            surface.drawStringCentered("Click here to add a name", i + c / 2, (j + c1) - 3, 1, l2);
        }
        if(!flag)
            return;
        i = super.mouseX - (surface.menuMaxWidth - 199);
        j = super.mouseY - 36;
        if(i >= 0 && j >= 0 && i < 196 && j < 182) {
            friendsMenu.updateActions(i + (surface.menuMaxWidth - 199), j + 36, super.lastMouseDownButton, super.mouseDownButton);
            if(j <= 24 && mouseButtonClick == 1)
                if(i < 98 && anInt981 == 1) {
                    anInt981 = 0;
                    friendsMenu.method165(friendsMenuHandle);
                } else if(i > 98 && anInt981 == 0) {
                    anInt981 = 1;
                    friendsMenu.method165(friendsMenuHandle);
                }
            if(mouseButtonClick == 1 && anInt981 == 0) {
                int i2 = friendsMenu.selectedListIndex(friendsMenuHandle);
                if((i2 >= 0) && (mouseX < gameWidth - 20))
                    if(mouseX > gameWidth - 75) {
                        removeFromFriends(super.friendsListLongs[i2]);
                    } else if(super.friendsListOnlineStatus[i2] != 0) {
                        inputBoxType = 2;
                        privateMessageTarget = super.friendsListLongs[i2];
                        super.inputMessage = "";
                        super.enteredMessage = "";
                    }
            }
            if(mouseButtonClick == 1 && anInt981 == 1) {
                int j2 = friendsMenu.selectedListIndex(friendsMenuHandle);
                if((j2 >= 0) && (mouseX < gameWidth - 20) && (mouseX > gameWidth - 75))
                    removeFromIgnoreList(super.ignoreListLongs[j2]);
            }
            if(j > 166 && mouseButtonClick == 1 && anInt981 == 0) {
                inputBoxType = 1;
                super.inputText = "";
                super.enteredText = "";
            }
            if(j > 166 && mouseButtonClick == 1 && anInt981 == 1) {
                inputBoxType = 3;
                super.inputText = "";
                super.enteredText = "";
            }
            mouseButtonClick = 0;
        }
    }

    private void drawGame() {
        if(playerAliveTimeout != 0) {
            surface.fadePixels();
            surface.drawStringCentered("Oh dear! You are dead...", windowWidth / 2, windowHeight / 2, 7, 0xff0000);
            drawChatMessageTabs();
            surface.draw(aGraphics936, 0, 0);
            return;
        }
        if(sleeping) {
            surface.fadePixels();
            if(Math.random() < 0.14999999999999999D)
                surface.drawStringCentered("ZZZ", (int) (Math.random() * 80D), (int) (Math.random() * 334D), 5, (int) (Math.random() * 16777215D));
            if(Math.random() < 0.14999999999999999D)
                surface.drawStringCentered("ZZZ", 512 - (int) (Math.random() * 80D), (int) (Math.random() * 334D), 5, (int) (Math.random() * 16777215D));
            surface.drawBox(windowWidth / 2 - 100, 160, 200, 40, 0);
            surface.drawStringCentered("You are sleeping", windowWidth / 2, 50, 7, 0xffff00);
            surface.drawStringCentered("Fatigue: " + (sleepFatigue * 100) / 750 + "%", windowWidth / 2, 90, 7, 0xffff00);
            surface.drawStringCentered("When you want to wake up just use your", windowWidth / 2, 140, 5, 0xffffff);
            surface.drawStringCentered("keyboard to type the word in the box below", windowWidth / 2, 160, 5, 0xffffff);
            surface.drawStringCentered(super.inputText + "*", windowWidth / 2, 180, 5, 65535);
            if(sleepScreenMessage == null)
                //	surface.drawSprite(windowWidth / 2 - 127, 230,
                //			SPRITE_TEXTURE + 1);
                surface.drawPixels(captchaPixels, windowWidth / 2 - 127, 230, captchaWidth, captchaHeight);
            else
                surface.drawStringCentered(sleepScreenMessage, windowWidth / 2, 260, 5, 0xff0000);
            surface.drawBoxEdge(windowWidth / 2 - 128, 229, 257, 42, 0xffffff);
            drawChatMessageTabs();
            surface.drawStringCentered("If you can't read the word", windowWidth / 2, 290, 1, 0xffffff);
            surface.drawStringCentered("@yel@click here@whi@ to get a different one", windowWidth / 2, 305, 1, 0xffffff);
            surface.draw(aGraphics936, 0, 0);
            return;
        }
        if(showCharacterLookScreen) {
            method62();
            return;
        }
        if(!world.playerIsAlive)
            return;
        for(int i = 0; i < 64; i++) {
            scene.removeModel(world.aModelArrayArray598[lastWildYSubtract][i]);
            if(lastWildYSubtract == 0) {
                scene.removeModel(world.aModelArrayArray580[1][i]);
                scene.removeModel(world.aModelArrayArray598[1][i]);
                scene.removeModel(world.aModelArrayArray580[2][i]);
                scene.removeModel(world.aModelArrayArray598[2][i]);
            }
            zoomCamera = true;
            if(lastWildYSubtract == 0 && (world.modelAdjacency[ourPlayer.currentX / 128][ourPlayer.currentY / 128] & 0x80) == 0) {
                if(showRoofs) {
                    scene.addModel(world.aModelArrayArray598[lastWildYSubtract][i]);
                    if(lastWildYSubtract == 0) {
                        scene.addModel(world.aModelArrayArray580[1][i]);
                        scene.addModel(world.aModelArrayArray598[1][i]);
                        scene.addModel(world.aModelArrayArray580[2][i]);
                        scene.addModel(world.aModelArrayArray598[2][i]);
                    }
                }
                zoomCamera = false;
            }
        }
        if(modelFireLightningSpellNumber != anInt742) {
            anInt742 = modelFireLightningSpellNumber;
            for(int j = 0; j < objectCount; j++) {
                if(objectType[j] == 97)
                    method98(j, "firea" + (modelFireLightningSpellNumber + 1));
                if(objectType[j] == 274)
                    method98(j, "fireplacea" + (modelFireLightningSpellNumber + 1));
                if(objectType[j] == 1031)
                    method98(j, "lightning" + (modelFireLightningSpellNumber + 1));
                if(objectType[j] == 1036)
                    method98(j, "firespell" + (modelFireLightningSpellNumber + 1));
                if(objectType[j] == 1147)
                    method98(j, "spellcharge" + (modelFireLightningSpellNumber + 1));
            }
        }
        if(modelTorchNumber != anInt743) {
            anInt743 = modelTorchNumber;
            for(int k = 0; k < objectCount; k++) {
                if(objectType[k] == 51)
                    method98(k, "torcha" + (modelTorchNumber + 1));
                if(objectType[k] == 143)
                    method98(k, "skulltorcha" + (modelTorchNumber + 1));
            }
        }
        if(modelClawSpellNumber != anInt744) {
            anInt744 = modelClawSpellNumber;
            for(int l = 0; l < objectCount; l++)
                if(objectType[l] == 1142)
                    method98(l, "clawspell" + (modelClawSpellNumber + 1));
        }
        scene.reduceSprites(fightCount);
        fightCount = 0;
        for(int index = 0; index < playerCount; index++) {
            Actor player = playerArray[index];
            if(player == null) {
                debug("playerCount=" + playerCount);
                debug("player[" + index + "] == null");
            } else if(player.colorBottomType != 255) {
                int playerX = player.currentX;
                int playerY = player.currentY;
                int playerZ = -world.getElevation(playerX, playerY);
                int playerHandle = scene.drawSprite(5000 + index, playerX, playerZ, playerY, 145, 220, index + 10000);
                fightCount++;
                if(player == ourPlayer)
                    scene.setModelUnclickable(playerHandle);
                if(player.currentAnimation == 8)
                    scene.setModelOffset(playerHandle, -30);
                if(player.currentAnimation == 9)
                    scene.setModelOffset(playerHandle, 30);
            }
        }
        for(int j1 = 0; j1 < playerCount; j1++) {
            Actor player = playerArray[j1];
            if(player.anInt176 > 0) {
                Actor npc = null;
                if(player.attackingNpcIndex != -1)
                    npc = npcRecordArray[player.attackingNpcIndex];
                else if(player.attackingMobIndex != -1)
                    npc = mobArray[player.attackingMobIndex];
                if(npc != null) {
                    int px = player.currentX;
                    int py = player.currentY;
                    int pi = -world.getElevation(px, py) - 110;
                    int nx = npc.currentX;
                    int ny = npc.currentY;
                    int ni = -world.getElevation(nx, ny) - Data.npcCameraArray2[npc.id] / 2;
                    int i10 = (px * player.anInt176 + nx * (PROJECTILE_SPEED - player.anInt176)) / PROJECTILE_SPEED;
                    int j10 = (pi * player.anInt176 + ni * (PROJECTILE_SPEED - player.anInt176)) / PROJECTILE_SPEED;
                    int k10 = (py * player.anInt176 + ny * (PROJECTILE_SPEED - player.anInt176)) / PROJECTILE_SPEED;
                    scene.drawSprite(SPRITE_PROJECTILE + player.attackingCameraInt, i10, j10, k10, 32, 32, 0);
                    fightCount++;
                }
            }
        }
        for(int l1 = 0; l1 < npcCount; l1++) {
            Actor npc = npcs[l1];
            if(npc == null)
                continue;
            int mobx = npc.currentX;
            int moby = npc.currentY;
            int i7 = -world.getElevation(mobx, moby);

            int i9 = scene.drawSprite(20000 + l1, mobx, i7, moby, Data.npcHovering[npc.id], Data.npcCameraArray2[npc.id], l1 + 30000);

/*			int i9 = scene.drawSprite(20000 + l1, mobx, i7, moby,
					Data.npcWidth[npc.id], Data.npcHeight[npc.id], l1 + 30000);*/
            fightCount++;
            if(npc.currentAnimation == 8)
                scene.setModelOffset(i9, -30);
            if(npc.currentAnimation == 9)
                scene.setModelOffset(i9, 30);
            if(npc.anInt176 > 0) {
                Actor target = null;
                if(npc.attackingNpcIndex != -1)
                    target = npcRecordArray[npc.attackingNpcIndex];
                else if(npc.attackingMobIndex != -1)
                    target = mobArray[npc.attackingMobIndex];
                if(target != null) {
                    int px = npc.currentX;
                    int py = npc.currentY;
                    int pi = -world.getElevation(px, py) - 110;
                    int nx = target.currentX;
                    int ny = target.currentY;
                    int ni = -world.getElevation(nx, ny) - Data.npcCameraArray2[target.id] / 2;
                    int i10 = (px * npc.anInt176 + nx * (PROJECTILE_SPEED - npc.anInt176)) / PROJECTILE_SPEED;
                    int j10 = (pi * npc.anInt176 + ni * (PROJECTILE_SPEED - npc.anInt176)) / PROJECTILE_SPEED;
                    int k10 = (py * npc.anInt176 + ny * (PROJECTILE_SPEED - npc.anInt176)) / PROJECTILE_SPEED;
                    scene.drawSprite(SPRITE_PROJECTILE + npc.attackingCameraInt, i10, j10, k10, 32, 32, 0);
                    fightCount++;
                }
            }
        }
        for(int j2 = 0; j2 < groundItemCount; j2++) {
            int j3 = groundItemX[j2] * magicLoc + 64;
            int k4 = groundItemY[j2] * magicLoc + 64;
            scene.drawSprite(40000 + groundItemType[j2], j3, -world.getElevation(j3, k4) - groundItemObjectVar[j2], k4, 96, 64, j2 + 20000);
            fightCount++;
        }
        for(int k3 = 0; k3 < anInt892; k3++) {
            int l4 = anIntArray944[k3] * magicLoc + 64;
            int j7 = anIntArray757[k3] * magicLoc + 64;
            int j9 = anIntArray782[k3];
            if(j9 == 0) {
                scene.drawSprite(50000 + k3, l4, -world.getElevation(l4, j7), j7, 128, gameWidth / 2, k3 + 50000);
                fightCount++;
            }
            if(j9 == 1) {
                scene.drawSprite(50000 + k3, l4, -world.getElevation(l4, j7), j7, 128, 64, k3 + 50000);
                fightCount++;
            }
        }
        surface.interlace = false;
        surface.blackScreen();
        surface.interlace = super.keyF1Toggle;
        if(lastWildYSubtract == 3) {
            int i5 = 40 + (int) (Math.random() * 3D);
            int k7 = 40 + (int) (Math.random() * 7D);
            scene.method304(i5, k7, -50, -10, -50);
        }
        anInt699 = 0;
        mobMessageCount = 0;
        anInt718 = 0;
        if(cameraAutoAngleDebug) {
            if(configAutoCameraAngle && !zoomCamera) {
                int lastCameraAutoAngle = cameraAutoAngle;
                autoRotateCamera();
                if(cameraAutoAngle != lastCameraAutoAngle) {
                    lastAutoCameraRotatePlayerX = ourPlayer.currentX;
                    lastAutoCameraRotatePlayerY = ourPlayer.currentY;
                }
            }
            scene.clipFar3D = 3000;
            scene.clipFar2D = 3000;
            scene.fogZFalloff = 1;
            scene.fogZDistance = 2800;
            cameraRotation = cameraAutoAngle * 32;
            int k5 = lastAutoCameraRotatePlayerX + screenRotationX;
            int l7 = lastAutoCameraRotatePlayerY + screenRotationY;
            scene.setCamera(k5, -world.getElevation(k5, l7), l7, 912, cameraRotation * 4, 0, 2000);
        } else {
            if(configAutoCameraAngle && !zoomCamera)
                autoRotateCamera();
            if(!FOG_ENABLED) {
                scene.clipFar3D = 65535;
                scene.clipFar2D = 65535;
                scene.fogZFalloff = 1;
                scene.fogZDistance = 65535;
            } else {
                scene.clipFar3D = 2200;
                scene.clipFar2D = 2200;
                scene.fogZFalloff = 1;
                scene.fogZDistance = 2100;
            }
            int l5 = lastAutoCameraRotatePlayerX + screenRotationX;
            int i8 = lastAutoCameraRotatePlayerY + screenRotationY;
            scene.setCamera(l5, -world.getElevation(l5, i8), i8, 912, cameraRotation * 4, 0, cameraHeight * 2);
        }
        scene.finishCamera();
        method119();
        if(actionPictureType > 0)
            surface.drawSprite(actionPictureX - 8, actionPictureY - 8, SPRITE_MEDIA + 14 + (24 - actionPictureType) / 6);
        if(actionPictureType < 0)
            surface.drawSprite(actionPictureX - 8, actionPictureY - 8, SPRITE_MEDIA + 18 + (24 + actionPictureType) / 6);
        if(systemUpdate != 0) {
            int i6 = systemUpdate / 50;
            int j8 = i6 / 60;
            i6 %= 60;
            if(i6 < 10)
                surface.drawStringCentered("System update in: " + j8 + ":0" + i6, gameWidth / 2, windowHeight - 7, 1, 0xffff00);
            else
                surface.drawStringCentered("System update in: " + j8 + ":" + i6, gameWidth / 2, windowHeight - 7, 1, 0xffff00);
        }
        if(!notInWilderness) {
            int j6 = 2203 - (regionY + wildY + areaY);
            // debug("pid=131, y="+(sectionY+areaY)+", wild="+wildY+", height="+wildYSubtract+", wildYMultiplier="+wildYMultiplier);
            if(regionX + wildX + areaX >= 2640)
                j6 = -50;
            if(j6 > 0) {
                // System.out.println("sectionY="+sectionY+",areaY="+areaY+",y="+(sectionY+areaY)+",j6="+j6+",wildY="+wildY);
                int k8 = 1 + j6 / 6;
                surface.drawSprite(windowWidth - 58, windowHeight - 58, SPRITE_MEDIA + 13);
                surface.drawStringCentered("Wilderness", windowWidth - 47, windowHeight - 20, 1, 0xffff00);
                surface.drawStringCentered("Level: " + k8, windowWidth - 47, windowHeight - 7, 1, 0xffff00);
                if(wildernessType == 0)
                    wildernessType = 2;
            }
            if(wildernessType == 0 && j6 > -10)
                wildernessType = 1;
        }
        // if (showIGPing) {
        double igping = (PING_RECIEVED - PING_SENT) / 1e6;
        if(igping > 0)
            surface.drawStringCentered("@whi@ping: @gre@" + df2.format(igping) + "@whi@ms", 50, 130, 1, 0xffff00);
        // }
        if(messagesTab == 0) {
            for(int k6 = 0; k6 < 5; k6++)
                if(messagesTimeout[k6] > 0) {
                    String s = messagesArray[k6];
                    surface.drawString(s, 7, windowHeight - 18 - k6 * 12, 1, 0xffff00);
                }
        }
        gameMenu.method171(generalChatHandle);
        gameMenu.method171(questChatHandle);
        gameMenu.method171(messagesHandleType6);
        switch(messagesTab) {
            case 1:
                gameMenu.method170(generalChatHandle);
                break;
            case 2:
                gameMenu.method170(questChatHandle);
                break;
            case 3:
                gameMenu.method170(messagesHandleType6);
                break;
            default:
                break;
        }
        Menu.anInt225 = 2;
        gameMenu.drawMenu();
        Menu.anInt225 = 0;
        surface.fade(surface.menuMaxWidth - 3 - 197, 3, SPRITE_MEDIA, 128);
        drawGameWindowsMenus();
        surface.loggedIn = false;
        drawChatMessageTabs();
        surface.draw(aGraphics936, 0, 0);
    }

    private void drawGameMenu() {
        if(gameMenu != null) {
            gameMenu.resize(generalChatHandle, 5, gameHeight - 65, gameWidth - 14, 56);
            gameMenu.resize(chatInputHandle, 7, gameHeight - 10, gameWidth - 14, 14);
            gameMenu.resize(questChatHandle, 5, gameHeight - 65, gameWidth - 14, 56);
            gameMenu.resize(messagesHandleType6, 5, gameHeight - 65, gameWidth - 14, 56);
        } else {
            gameMenu = new Menu(surface, 10);
            generalChatHandle = gameMenu.method159(5, gameHeight - 65, gameWidth - 14, 56, 1, 20, true);
            chatInputHandle = gameMenu.addChatInput(7, gameHeight - 10, gameWidth - 14, 14, 1, 80, false, true);
            questChatHandle = gameMenu.method159(5, gameHeight - 65, gameWidth - 14, 56, 1, 20, true);
            messagesHandleType6 = gameMenu.method159(5, gameHeight - 65, gameWidth - 14, 56, 1, 20, true);
            gameMenu.setFocus(chatInputHandle);
        }
    }

    private void drawGameWindowsMenus() {
        if(!showTradeWindow && (inputBoxType == 6 || inputBoxType == 7))
            inputBoxType = 0;
        if(!showDuelWindow && (inputBoxType == 8 || inputBoxType == 9))
            inputBoxType = 0;
        for(GraphicalOverlay overlay : GameUIs.overlay) {
            if(overlay.isVisible()) {
                overlay.onRender();
                if(rightClickOptions)
                    drawRightClickOptions();
                if(inputBoxType != 0)
                    drawInputBox();
                if(overlay.menu) {
                    mouseButtonClick = 0;
                    return;
                }
            }
        }
        if(logoutTimeout != 0)
            drawLoggingOutBox();
        else if(showServerMessageBox)
            drawServerMessageBox();
        else if(wildernessType == 1) // 0 = not wild, 1 = close to wild, 2 =
            // wild
            drawWildernessWarningBox();
        else if(showPetInventory) {
            showPetInventory();
            if(rightClickOptions)
                drawRightClickOptions();
        } else if(!GameUIs.overlay.get(0).isVisible() && inputBoxType == 4)
            inputBoxType = 0;
        else if(showShop && lastWalkTimeout == 0) {
            drawShopBox();
            if(inputBoxType != 0)
                drawInputBox();
        } else if(showTradeWindow) {
            drawTradeWindow();
            if(rightClickOptions)
                drawRightClickOptions();
            if(inputBoxType != 0)
                drawInputBox();
        } else if(showDuelWindow) {
            drawDuelWindow();
            if(rightClickOptions)
                drawRightClickOptions();
            if(inputBoxType != 0)
                drawInputBox();
        } else if(showTradeConfirmWindow)
            drawTradeConfirmWindow();
        else if(showDuelConfirmWindow)
            drawDuelConfirmWindow();
        else if(showAbuseWindow == 1)
            drawAbuseWindow1();
        else if(showAbuseWindow == 2)
            drawAbuseWindow2();
        else if(inputBoxType != 0) {
            drawInputBox();
        } else {
            if(showQuestionMenu)
                drawQuestionMenu();
            if(ourPlayer.currentAnimation == 8 || ourPlayer.currentAnimation == 9)
                drawCombatStyleWindow();
            checkMouseOverMenus();
            boolean noMenusShown = !showQuestionMenu && !showRightClickMenu;
            if(noMenusShown)
                menuLength = 0;
            if(mouseOverMenu == 0 && noMenusShown)
                drawInventoryRightClickMenu();
            if(mouseOverMenu == 1)
                drawInventoryMenu(noMenusShown);
            if(mouseOverMenu == 2)
                drawMapMenu(noMenusShown);
            if(mouseOverMenu == 3)
                drawPlayerInfoMenu(noMenusShown);
            if(mouseOverMenu == 4)
                drawMagicWindow(noMenusShown);
            if(mouseOverMenu == 5)
                drawFriendsWindow(noMenusShown);
            if(mouseOverMenu == 6)
                drawOptionsMenu(noMenusShown);
            if(noMenusShown)
                checkMouseStatus();
            if(showRightClickMenu && !showQuestionMenu)
                drawRightClickMenu();
        }
        mouseButtonClick = 0;
    }

    private void drawInputBox() {
        if(mouseButtonClick != 0) {
            mouseButtonClick = 0;
            if(inputBoxType == 1 && (super.mouseX < 106 || super.mouseY < 145 || super.mouseX > 406 || super.mouseY > 215)) {
                inputBoxType = 0;
                return;
            }
            if(inputBoxType == 2 && (super.mouseX < 6 || super.mouseY < 145 || super.mouseX > 506 || super.mouseY > 215)) {
                inputBoxType = 0;
                return;
            }
            if(inputBoxType == 3 && (super.mouseX < 106 || super.mouseY < 145 || super.mouseX > 406 || super.mouseY > 215)) {
                inputBoxType = 0;
                return;
            }
            if(inputBoxType == 4 && super.mouseX > windowWidth / 2 + 6 && super.mouseX < windowWidth / 2 + 46 && super.mouseY > windowHeight / 2 + 24 && super.mouseY < windowHeight / 2 + 37) {
                inputBoxType = 0;
                return;
            }
            if((inputBoxType == 6 || inputBoxType == 7 || inputBoxType == 8 || inputBoxType == 9) && super.mouseX > windowWidth / 2 + 6 && super.mouseX < windowWidth / 2 + 46 && super.mouseY > windowHeight / 2 + 8 && super.mouseY < windowHeight / 2 + 21) {
                clickScreenSend = true;
                inputBoxType = 0;
                return;
            }
            if(super.mouseX > 236 && super.mouseX < 276 && super.mouseY > 193 && super.mouseY < 213) {
                inputBoxType = 0;
                return;
            }
            if(inputBoxType == 4 && super.mouseX > windowWidth / 2 - 28 && super.mouseX < windowWidth / 2 - 10 && super.mouseY > windowHeight / 2 + 24 && super.mouseY < windowHeight / 2 + 37) {
                if(super.inputText.length() > 0) {
                    String s = super.inputText.trim();
                    super.inputText = "";
                    super.enteredText = "";
                    inputBoxType = 0;
                    int amount = -1;
                    try {
                        amount = Integer.parseInt(s);
                    } catch(NumberFormatException nfe) {
                        inputBoxType = 4;
                    }
                    if(amount != -1)
                        doBankFunction(amount);
                }
                return;
            }
            if(inputBoxType == 6 && super.mouseX > windowWidth / 2 - 28 && super.mouseX < windowWidth / 2 - 10 && super.mouseY > windowHeight / 2 + 8 && super.mouseY < windowHeight / 2 + 21) {
                if(super.inputText.length() > 0) {
                    String s = super.inputText.trim();
                    super.inputText = "";
                    super.enteredText = "";
                    addTradeItems(inputID, Integer.parseInt(s), 0, true);
                    inputBoxType = 0;
                }
                return;
            }
            if(inputBoxType == 7 && super.mouseX > windowWidth / 2 - 28 && super.mouseX < windowWidth / 2 - 10 && super.mouseY > windowHeight / 2 + 8 && super.mouseY < windowHeight / 2 + 21) {
                if(super.inputText.length() > 0) {
                    String s = super.inputText.trim();
                    super.inputText = "";
                    super.enteredText = "";
                    removeTradeItems(inputID, Integer.parseInt(s), 0);
                    inputBoxType = 0;
                }
                return;
            }
            if(inputBoxType == 8 && super.mouseX > windowWidth / 2 - 28 && super.mouseX < windowWidth / 2 - 10 && super.mouseY > windowHeight / 2 + 8 && super.mouseY < windowHeight / 2 + 21) {
                if(super.inputText.length() > 0) {
                    String s = super.inputText.trim();
                    super.inputText = "";
                    super.enteredText = "";
                    addDuelItems(inputID, Integer.parseInt(s), 0, false);
                    inputBoxType = 0;
                }
                return;
            }
            if(inputBoxType == 9 && super.mouseX > windowWidth / 2 - 28 && super.mouseX < windowWidth / 2 - 10 && super.mouseY > windowHeight / 2 + 8 && super.mouseY < windowHeight / 2 + 21) {
                if(super.inputText.length() > 0) {
                    String s = super.inputText.trim();
                    super.inputText = "";
                    super.enteredText = "";
                    removeDuelItems(inputID, Integer.parseInt(s), 0);
                    inputBoxType = 0;
                }
                return;
            }
            if(inputBoxType == 10 && super.mouseX > windowWidth / 2 - 28 && super.mouseX < windowWidth / 2 - 10 && super.mouseY > windowHeight / 2 + 8 && super.mouseY < windowHeight / 2 + 21) {
                if(super.inputText.length() > 0) {
                    String s = super.inputText.trim();
                    super.inputText = "";
                    super.enteredText = "";
                    super.streamClass.addNewFrame(178);
                    super.streamClass.addShort(shopItems[selectedShopItemIndex]);
                    super.streamClass.addInt(Integer.parseInt(s));
                    super.streamClass.formatCurrentFrame();
                    inputBoxType = 0;
                }
                return;
            }
            if(inputBoxType == 11 && super.mouseX > windowWidth / 2 - 28 && super.mouseX < windowWidth / 2 - 10 && super.mouseY > windowHeight / 2 + 8 && super.mouseY < windowHeight / 2 + 21) {
                if(super.inputText.length() > 0) {
                    String s = super.inputText.trim();
                    super.inputText = "";
                    super.enteredText = "";
                    super.streamClass.addNewFrame(245);
                    super.streamClass.addShort(shopItems[selectedShopItemIndex]);
                    super.streamClass.addInt(Integer.parseInt(s));
                    super.streamClass.formatCurrentFrame();
                    inputBoxType = 0;
                }
                return;
            }
        }
        int i = gameHeight / 2 - 35;
        if(inputBoxType == 1) {
            surface.drawBox(gameWidth / 2 - 150, i, 300, 70, 0);
            surface.drawBoxEdge(gameWidth / 2 - 150, i, 300, 70, 16777215);
            i += 20;
            surface.drawStringCentered("Enter name to add to friends list", gameWidth / 2, i, 4, 0xffffff);
            i += 20;
            surface.drawStringCentered(super.inputText + "*", gameWidth / 2, i, 4, 0xffffff);
            if(super.enteredText.length() > 0) {
                String s = super.enteredText.trim();
                super.inputText = "";
                super.enteredText = "";
                inputBoxType = 0;
                if(s.length() > 0 && Utility.base37Encode(s) != ourPlayer.nameLong)
                    addToFriendsList(s);
            }
        }
        if(inputBoxType == 2) {
            surface.drawBox(gameWidth / 2 - 250, gameHeight / 2 - 35, 500, 70, 0);
            surface.drawBoxEdge(gameWidth / 2 - 250, gameHeight / 2 - 35, 500, 70, 16777215);
            i += 20;
            surface.drawStringCentered("Enter message to send to " + Utility.base37Decode(privateMessageTarget), gameWidth / 2 - 15, gameHeight / 2 - 17, 4, 16777215);
            i += 20;
            surface.drawStringCentered(inputMessage + "*", gameWidth / 2, gameHeight / 2 + 5, 4, 16777215);
            if(enteredMessage.length() > 0) {
                String s1 = enteredMessage;
                inputMessage = "";
                enteredMessage = "";
                inputBoxType = 0;
                int messageLength = ChatMessage.stringToByteArray(s1);
                sendPrivateMessage(privateMessageTarget, ChatMessage.messageData, messageLength);
                s1 = ChatMessage.byteToString(ChatMessage.messageData, 0, messageLength);
                handleServerMessage("@pri@You tell " + Utility.base37Decode(privateMessageTarget) + ": " + s1);
            }
        }
        if(inputBoxType == 3) {
            surface.drawBox(gameWidth / 2 - 150, gameHeight / 2 - 35, 300, 70, 0);
            surface.drawBoxEdge(gameWidth / 2 - 150, gameHeight / 2 - 35, 300, 70, 16777215);
            i += 20;
            surface.drawStringCentered("Enter name to add to ignore list", gameWidth / 2, gameHeight / 2 - 15, 4, 16777215);
            i += 20;
            surface.drawStringCentered(inputText + "*", gameWidth / 2, gameHeight / 2 + 5, 4, 16777215);
            if(enteredText.length() > 0) {
                String s2 = enteredText.trim();
                inputText = "";
                enteredText = "";
                inputBoxType = 0;
                if((s2.length() > 0) && (Utility.base37Encode(s2) != ourPlayer.nameLong))
                    addToIgnoreList(s2);
            }
            int j = 16777215;
            if((mouseX > gameWidth / 2 - 20) && (mouseX < gameWidth / 2 + 20) && (mouseY > gameHeight / 2 + 15) && (mouseY < gameHeight / 2 + 35))
                j = 16776960;
            surface.drawStringCentered("Cancel", gameWidth / 2, gameHeight / 2 + 25, 1, j);
        }
        if(inputBoxType == 4) {
            surface.drawBox(windowWidth / 2 - 200, windowHeight / 2 - 35, 400, 78, 0);
            surface.drawBoxEdge(windowWidth / 2 - 200, windowHeight / 2 - 35, 400, 78, 0xffffff);
            surface.drawStringCentered("Please enter the number of items to " + (deposit ? "deposit" : "withdraw"), windowWidth / 2, windowHeight / 2 - 16, 1, 0xffff00);
            surface.drawStringCentered("and press enter", windowWidth / 2, windowHeight / 2, 1, 0xffff00);
            surface.drawStringCentered(super.inputText + "*", windowWidth / 2, windowHeight / 2 + 20, 4, 0xffffff);
            if(super.enteredText.length() > 0) {
                String s = super.enteredText.trim();
                super.inputText = "";
                super.enteredText = "";
                inputBoxType = 0;
                int amount = -1;
                try {
                    amount = Integer.parseInt(s);
                } catch(NumberFormatException numberFormatException) {
                    inputBoxType = 4;
                }
                if(amount != -1) {
                    doBankFunction(amount);
                }
            }
            int j = 0xffffff;
            if(super.mouseX > windowWidth / 2 - 28 && super.mouseX < windowWidth / 2 - 10 && super.mouseY > windowHeight / 2 + 24 && super.mouseY < windowHeight / 2 + 37)
                j = 0xffff00;
            surface.drawStringCentered("OK", windowWidth / 2 - 18, windowHeight / 2 + 36, 1, j);
            int k = 0xffffff;
            if(super.mouseX > windowWidth / 2 + 6 && super.mouseX < windowWidth / 2 + 46 && super.mouseY > windowHeight / 2 + 24 && super.mouseY < windowHeight / 2 + 37)
                k = 0xffff00;
            surface.drawStringCentered("Cancel", windowWidth / 2 + 27, windowHeight / 2 + 36, 1, k);
            return;
        }
        if(inputBoxType == 6) {
            surface.drawBox(windowWidth / 2 - 200, windowHeight / 2 - 35, 400, 62, 0);
            surface.drawBoxEdge(windowWidth / 2 - 200, windowHeight / 2 - 35, 400, 62, 0xffffff);
            surface.drawStringCentered("Enter number of items to offer and press enter", windowWidth / 2, windowHeight / 2 - 16, 1, 0xffff00);
            surface.drawStringCentered(super.inputText + "*", windowWidth / 2, windowHeight / 2 + 4, 4, 0xffffff);
            if(super.enteredText.length() > 0) {
                String s = super.enteredText.trim();
                super.inputText = "";
                super.enteredText = "";
                addTradeItems(inputID, Integer.parseInt(s), 0, true);
                inputBoxType = 0;
            }
            int j = 0xffffff;
            if(super.mouseX > windowWidth / 2 - 28 && super.mouseX < windowWidth / 2 - 10 && super.mouseY > windowHeight / 2 + 8 && super.mouseY < windowHeight / 2 + 21)
                j = 0xffff00;
            surface.drawStringCentered("OK", windowWidth / 2 - 18, windowHeight / 2 + 20, 1, j);
            int k = 0xffffff;
            if(super.mouseX > windowWidth / 2 + 6 && super.mouseX < windowWidth / 2 + 46 && super.mouseY > windowHeight / 2 + 8 && super.mouseY < windowHeight / 2 + 21)
                k = 0xffff00;
            surface.drawStringCentered("Cancel", windowWidth / 2 + 27, windowHeight / 2 + 20, 1, k);
            return;
        }
        if(inputBoxType == 7) {
            surface.drawBox(windowWidth / 2 - 200, windowHeight / 2 - 35, 400, 62, 0);
            surface.drawBoxEdge(windowWidth / 2 - 200, windowHeight / 2 - 35, 400, 62, 0xffffff);
            surface.drawStringCentered("Enter number of items to remove and press enter", windowWidth / 2, windowHeight / 2 - 16, 1, 0xffff00);
            surface.drawStringCentered(super.inputText + "*", windowWidth / 2, windowHeight / 2 + 4, 4, 0xffffff);
            if(super.enteredText.length() > 0) {
                String s = super.enteredText.trim();
                if(containsOnlyNumbers(s)) {
                    super.inputText = "";
                    super.enteredText = "";
                    removeTradeItems(inputID, Integer.parseInt(s), 0);
                    inputBoxType = 0;
                }
            }
            int j = 0xffffff;
            if(super.mouseX > windowWidth / 2 - 28 && super.mouseX < windowWidth / 2 - 10 && super.mouseY > windowHeight / 2 + 8 && super.mouseY < windowHeight / 2 + 21)
                j = 0xffff00;
            surface.drawStringCentered("OK", windowWidth / 2 - 18, windowHeight / 2 + 20, 1, j);
            int k = 0xffffff;
            if(super.mouseX > windowWidth / 2 + 6 && super.mouseX < windowWidth / 2 + 46 && super.mouseY > windowHeight / 2 + 8 && super.mouseY < windowHeight / 2 + 21)
                k = 0xffff00;
            surface.drawStringCentered("Cancel", windowWidth / 2 + 27, windowHeight / 2 + 20, 1, k);
            return;
        }
        if(inputBoxType == 8) {
            surface.drawBox(windowWidth / 2 - 200, windowHeight / 2 - 35, 400, 62, 0);
            surface.drawBoxEdge(windowWidth / 2 - 200, windowHeight / 2 - 35, 400, 62, 0xffffff);
            surface.drawStringCentered("Enter number of items to stake and press enter", windowWidth / 2, windowHeight / 2 - 16, 1, 0xffff00);
            surface.drawStringCentered(super.inputText + "*", windowWidth / 2, windowHeight / 2 + 4, 4, 0xffffff);
            if(super.enteredText.length() > 0) {
                String s = super.enteredText.trim();
                if(containsOnlyNumbers(s)) {
                    super.inputText = "";
                    super.enteredText = "";
                    addDuelItems(inputID, Integer.parseInt(s), 0, false);
                    inputBoxType = 0;
                }
            }
            int j = 0xffffff;
            if(super.mouseX > windowWidth / 2 - 28 && super.mouseX < windowWidth / 2 - 10 && super.mouseY > windowHeight / 2 + 8 && super.mouseY < windowHeight / 2 + 21)
                j = 0xffff00;
            surface.drawStringCentered("OK", windowWidth / 2 - 18, windowHeight / 2 + 20, 1, j);
            int k = 0xffffff;
            if(super.mouseX > windowWidth / 2 + 6 && super.mouseX < windowWidth / 2 + 46 && super.mouseY > windowHeight / 2 + 8 && super.mouseY < windowHeight / 2 + 21)
                k = 0xffff00;
            surface.drawStringCentered("Cancel", windowWidth / 2 + 27, windowHeight / 2 + 20, 1, k);
            return;
        }
        if(inputBoxType == 9) {
            surface.drawBox(windowWidth / 2 - 200, windowHeight / 2 - 35, 400, 62, 0);
            surface.drawBoxEdge(windowWidth / 2 - 200, windowHeight / 2 - 35, 400, 62, 0xffffff);
            surface.drawStringCentered("Enter number of items to remove and press enter", windowWidth / 2, windowHeight / 2 - 16, 1, 0xffff00);
            surface.drawStringCentered(super.inputText + "*", windowWidth / 2, windowHeight / 2 + 4, 4, 0xffffff);
            if(super.enteredText.length() > 0) {
                String s = super.enteredText.trim();
                if(containsOnlyNumbers(s)) {
                    super.inputText = "";
                    super.enteredText = "";
                    removeDuelItems(inputID, Integer.parseInt(s), 0);
                    inputBoxType = 0;
                }
            }
            int j = 0xffffff;
            if(super.mouseX > windowWidth / 2 - 28 && super.mouseX < windowWidth / 2 - 10 && super.mouseY > windowHeight / 2 + 8 && super.mouseY < windowHeight / 2 + 21)
                j = 0xffff00;
            surface.drawStringCentered("OK", windowWidth / 2 - 18, windowHeight / 2 + 20, 1, j);
            int k = 0xffffff;
            if(super.mouseX > windowWidth / 2 + 6 && super.mouseX < windowWidth / 2 + 46 && super.mouseY > windowHeight / 2 + 8 && super.mouseY < windowHeight / 2 + 21)
                k = 0xffff00;
            surface.drawStringCentered("Cancel", windowWidth / 2 + 27, windowHeight / 2 + 20, 1, k);
            return;
        }
        if(inputBoxType == 10) {
            surface.drawBox(windowWidth / 2 - 200, windowHeight / 2 - 35, 400, 62, 0);
            surface.drawBoxEdge(windowWidth / 2 - 200, windowHeight / 2 - 35, 400, 62, 0xffffff);
            surface.drawStringCentered("Enter number of items to buy and press enter", windowWidth / 2, windowHeight / 2 - 16, 1, 0xffff00);
            surface.drawStringCentered(super.inputText + "*", windowWidth / 2, windowHeight / 2 + 4, 4, 0xffffff);
            if(super.enteredText.length() > 0) {
                String s = super.enteredText.trim();
                if(containsOnlyNumbers(s)) {
                    super.inputText = "";
                    super.enteredText = "";
                    super.streamClass.addNewFrame(187);
                    super.streamClass.addShort(inputID);
                    super.streamClass.addInt(Integer.parseInt(s));
                    super.streamClass.formatCurrentFrame();
                    inputBoxType = 0;
                }
            }
            int j = 0xffffff;
            if(super.mouseX > windowWidth / 2 - 28 && super.mouseX < windowWidth / 2 - 10 && super.mouseY > windowHeight / 2 + 8 && super.mouseY < windowHeight / 2 + 21)
                j = 0xffff00;
            surface.drawStringCentered("OK", windowWidth / 2 - 18, windowHeight / 2 + 20, 1, j);
            int k = 0xffffff;
            if(super.mouseX > windowWidth / 2 + 6 && super.mouseX < windowWidth / 2 + 46 && super.mouseY > windowHeight / 2 + 8 && super.mouseY < windowHeight / 2 + 21)
                k = 0xffff00;
            surface.drawStringCentered("Cancel", windowWidth / 2 + 27, windowHeight / 2 + 20, 1, k);
            return;
        }
        if(inputBoxType == 11) {
            surface.drawBox(windowWidth / 2 - 200, windowHeight / 2 - 35, 400, 62, 0);
            surface.drawBoxEdge(windowWidth / 2 - 200, windowHeight / 2 - 35, 400, 62, 0xffffff);
            surface.drawStringCentered("Enter number of items to sell and press enter", windowWidth / 2, windowHeight / 2 - 16, 1, 0xffff00);
            surface.drawStringCentered(super.inputText + "*", windowWidth / 2, windowHeight / 2 + 4, 4, 0xffffff);
            if(super.enteredText.length() > 0) {
                String s = super.enteredText.trim();
                if(containsOnlyNumbers(s)) {
                    super.inputText = "";
                    super.enteredText = "";
                    super.streamClass.addNewFrame(245);
                    super.streamClass.addShort(inputID);
                    super.streamClass.addInt(Integer.parseInt(s));
                    super.streamClass.formatCurrentFrame();
                    inputBoxType = 0;
                }
            }
            int j = 0xffffff;
            if(super.mouseX > windowWidth / 2 - 28 && super.mouseX < windowWidth / 2 - 10 && super.mouseY > windowHeight / 2 + 8 && super.mouseY < windowHeight / 2 + 21)
                j = 0xffff00;
            surface.drawStringCentered("OK", windowWidth / 2 - 18, windowHeight / 2 + 20, 1, j);
            int k = 0xffffff;
            if(super.mouseX > windowWidth / 2 + 6 && super.mouseX < windowWidth / 2 + 46 && super.mouseY > windowHeight / 2 + 8 && super.mouseY < windowHeight / 2 + 21)
                k = 0xffff00;
            surface.drawStringCentered("Cancel", windowWidth / 2 + 27, windowHeight / 2 + 20, 1, k);
            return;
        }
        int j = 0xffffff;
        if((mouseX > gameWidth / 2 - 20) && (mouseX < gameWidth / 2 + 20) && (mouseY > gameHeight / 2 + 15) && (mouseY < gameHeight / 2 + 35))
            j = 16776960;
        surface.drawStringCentered("Cancel", gameWidth / 2, gameHeight / 2 + 25, 1, j);
    }

    private void drawInventoryMenu(boolean flag) {
        int i = surface.menuMaxWidth - 248;
        surface.drawSprite(i, 3, SPRITE_MEDIA + 1);
        for(int j = 0; j < anInt882; j++) {
            int k = i + (j % 5) * 49;
            int i1 = 36 + (j / 5) * 34;
            if(j < inventoryCount && wearing[j] == 1)
                surface.drawBoxAlpha(k, i1, 49, 34, 0xff0000, 128);
            else
                surface.drawBoxAlpha(k, i1, 49, 34, Surface.convertRGBToLong(181, 181, 181), 128);
            if(j < inventoryCount) {
                int mask = inventoryItems[j] == 183 ? ourPlayer.cape : Data.itemPictureMask[inventoryItems[j]];
                surface.spriteClip4(k, i1, 48, 32, SPRITE_ITEM + Data.itemInventoryPicture[inventoryItems[j]], mask, 0, 0, false);
                if(Data.itemStackable[inventoryItems[j]] == 0)
                    surface.drawString(insertCommas(String.valueOf(inventoryItemsCount[j])), k + 1, i1 + 10, 1, 0xffff00);
            }
        }
        for(int l = 1; l <= 4; l++)
            surface.drawLineY(i + l * 49, 36, (anInt882 / 5) * 34, 0);
        for(int j1 = 1; j1 <= anInt882 / 5 - 1; j1++)
            surface.drawLineX(i, 36 + j1 * 34, 245, 0);
        if(!flag)
            return;
        i = super.mouseX - (surface.menuMaxWidth - 248);
        int k1 = super.mouseY - 36;
        if(i >= 0 && k1 >= 0 && i < 248 && k1 < (anInt882 / 5) * 34) {
            int currentInventorySlot = i / 49 + (k1 / 34) * 5;
            if(currentInventorySlot < inventoryCount) {
                int i2 = inventoryItems[currentInventorySlot];
                if(selectedSpell >= 0) {
                    if(Data.spellType[selectedSpell] == 3) {
                        menuText1[menuLength] = "Cast " + Data.spellName[selectedSpell] + " on";
                        menuText2[menuLength] = "@lre@" + Data.itemName[i2];
                        menuID[menuLength] = 600;
                        menuActionType[menuLength] = currentInventorySlot;
                        menuActionVariable[menuLength] = selectedSpell;
                        menuLength++;
                    }
                } else {
                    if(selectedItem >= 0) {
                        menuText1[menuLength] = "Use " + selectedItemName + " with";
                        menuText2[menuLength] = "@lre@" + Data.itemName[i2];
                        menuID[menuLength] = 610;
                        menuActionType[menuLength] = currentInventorySlot;
                        menuActionVariable[menuLength] = selectedItem;
                        menuLength++;
                        return;
                    }
                    if(wearing[currentInventorySlot] == 1) {
                        menuText1[menuLength] = "Remove";
                        menuText2[menuLength] = "@lre@" + Data.itemName[i2];
                        menuID[menuLength] = 620;
                        menuActionType[menuLength] = currentInventorySlot;
                        menuLength++;
                    } else if(Data.itemWieldable[i2] != 0) {
                        if((Data.itemWieldable[i2] & 0x18) != 0)
                            menuText1[menuLength] = "Wield";
                        else
                            menuText1[menuLength] = "Wear";
                        menuText2[menuLength] = "@lre@" + Data.itemName[i2];
                        menuID[menuLength] = 630;
                        menuActionType[menuLength] = currentInventorySlot;
                        menuLength++;
                    }
                    if(!Data.itemCommand[i2].equals("")) {
                        menuText1[menuLength] = Data.itemCommand[i2];
                        menuText2[menuLength] = "@lre@" + Data.itemName[i2];
                        menuID[menuLength] = 640;
                        menuActionType[menuLength] = currentInventorySlot;
                        menuLength++;
                    }
                    menuText1[menuLength] = "Use";
                    menuText2[menuLength] = "@lre@" + Data.itemName[i2];
                    menuID[menuLength] = 650;
                    menuActionType[menuLength] = currentInventorySlot;
                    menuLength++;
                    menuText1[menuLength] = "Drop";
                    menuText2[menuLength] = "@lre@" + Data.itemName[i2];
                    menuID[menuLength] = 660;
                    menuActionType[menuLength] = currentInventorySlot;
                    menuLength++;
                    menuText1[menuLength] = "Examine";
                    menuText2[menuLength] = "@lre@" + Data.itemName[i2] + "(" + i2 + ")";
                    menuID[menuLength] = 3600;
                    menuActionType[menuLength] = i2;
                    menuLength++;
                }
            }
        }
    }

    private void drawInventoryRightClickMenu() {
        int i = 2203 - (regionY + wildY + areaY);
        if(regionX + wildX + areaX >= 2640)
            i = -50;
        int j = -1;
        for(int k = 0; k < objectCount; k++)
            aBooleanArray827[k] = false;
        for(int l = 0; l < wallObjectCount; l++)
            aBooleanArray970[l] = false;
        int i1 = scene.method272();
        GameModel[] models = scene.getVisibleModels();
        int ai[] = scene.method273();
        for(int j1 = 0; j1 < i1; j1++) {
            if(menuLength > 200)
                break;
            int k1 = ai[j1];
            GameModel model = models[j1];
            if(model.faceTag[k1] <= 65535 || model.faceTag[k1] >= 0x30d40 && model.faceTag[k1] <= 0x493e0)
                if(model == scene.activeModel) {
                    int i2 = model.faceTag[k1] % 10000;
                    int l2 = model.faceTag[k1] / 10000;
                    switch(l2) {
                        case 1:
                            String s = "";
                            int k3 = 0;
                            if(ourPlayer.level > 0 && playerArray[i2].level > 0)
                                k3 = ourPlayer.level - playerArray[i2].level;
                            if(k3 < 0)
                                s = "@or1@";
                            if(k3 < -3)
                                s = "@or2@";
                            if(k3 < -6)
                                s = "@or3@";
                            if(k3 < -9)
                                s = "@red@";
                            if(k3 > 0)
                                s = "@gr1@";
                            if(k3 > 3)
                                s = "@gr2@";
                            if(k3 > 6)
                                s = "@gr3@";
                            if(k3 > 9)
                                s = "@gre@";
                            s = " " + s + "(level-" + playerArray[i2].level + ")";
                            String clan = "";
                            if(selectedSpell >= 0) {
                                if(Data.spellType[selectedSpell] == 1 || Data.spellType[selectedSpell] == 2) {
                                    menuText1[menuLength] = "Cast " + Data.spellName[selectedSpell] + " on";
                                    menuText2[menuLength] = "@whi@" + clan + getPrefix(playerArray[i2].group) + playerArray[i2].name + s;
                                    menuID[menuLength] = 800;
                                    menuActionX[menuLength] = playerArray[i2].currentX;
                                    menuActionY[menuLength] = playerArray[i2].currentY;
                                    menuActionType[menuLength] = playerArray[i2].serverIndex;
                                    menuActionVariable[menuLength] = selectedSpell;
                                    menuLength++;
                                }
                            } else if(selectedItem >= 0) {
                                menuText1[menuLength] = "Use " + selectedItemName + " with";
                                menuText2[menuLength] = "@whi@" + clan + getPrefix(playerArray[i2].group) + playerArray[i2].name + s;
                                menuID[menuLength] = 810;
                                menuActionX[menuLength] = playerArray[i2].currentX;
                                menuActionY[menuLength] = playerArray[i2].currentY;
                                menuActionType[menuLength] = playerArray[i2].serverIndex;
                                menuActionVariable[menuLength] = selectedItem;
                                menuLength++;
                            } else {
                                if(i > 0 && (playerArray[i2].currentY - 64) / magicLoc + wildY + areaY < 2203 || (ourPlayer.group == 2)) {
                                    menuText1[menuLength] = "Attack";
                                    menuText2[menuLength] = "@whi@" + clan + getPrefix(playerArray[i2].group) + playerArray[i2].name + s;
                                    if(k3 >= 0 && k3 < 5)
                                        menuID[menuLength] = 805;
                                    else
                                        menuID[menuLength] = 2805;
                                    menuActionX[menuLength] = playerArray[i2].currentX;
                                    menuActionY[menuLength] = playerArray[i2].currentY;
                                    menuActionType[menuLength] = playerArray[i2].serverIndex;
                                    menuLength++;
                                }
                                if(member && (playerArray[i2].currentY - 64) / magicLoc + wildY + areaY > 2203) {
                                    menuText1[menuLength] = "Duel with";
                                    menuText2[menuLength] = "@whi@" + clan + getPrefix(playerArray[i2].group) + playerArray[i2].name + s;
                                    menuActionX[menuLength] = playerArray[i2].currentX;
                                    menuActionY[menuLength] = playerArray[i2].currentY;
                                    menuID[menuLength] = 2806;
                                    menuActionType[menuLength] = playerArray[i2].serverIndex;
                                    menuLength++;
                                }
                                /*
                                 * if (userGroup == 2) { menuText1[menuLength] =
                                 * "Attack"; menuText2[menuLength] = "@whi@" +
                                 * getPrefix(playerArray[i2].group) +
                                 * playerArray[i2].name + s; if (k3 >= 0 && k3 < 5)
                                 * menuID[menuLength] = 805; else menuID[menuLength]
                                 * = 2805; menuActionX[menuLength] =
                                 * playerArray[i2].currentX; menuActionY[menuLength]
                                 * = playerArray[i2].currentY;
                                 * menuActionType[menuLength] =
                                 * playerArray[i2].serverIndex; menuLength++; } if
                                 * (inBounds(598, 699, 616, 712)) {
                                 * menuText1[menuLength] = "Duel with";
                                 * menuText2[menuLength] = "@whi@" +
                                 * getPrefix(playerArray[i2].group) +
                                 * playerArray[i2].name + s; menuActionX[menuLength]
                                 * = playerArray[i2].currentX;
                                 * menuActionY[menuLength] =
                                 * playerArray[i2].currentY; menuID[menuLength] =
                                 * 2806; menuActionType[menuLength] =
                                 * playerArray[i2].serverIndex; menuLength++; }
                                 */
                                menuText1[menuLength] = "Trade with";
                                menuText2[menuLength] = "@whi@" + clan + getPrefix(playerArray[i2].group) + playerArray[i2].name + s;
                                menuID[menuLength] = 2810;
                                menuActionType[menuLength] = playerArray[i2].serverIndex;
                                menuLength++;
                                if(userGroup >= 1 && userGroup > playerArray[i2].group) {
                                    menuText1[menuLength] = "Kick";
                                    menuText2[menuLength] = "@whi@" + clan + getPrefix(playerArray[i2].group) + playerArray[i2].name + s;
                                    menuID[menuLength] = 2830;
                                    menuActionType[menuLength] = playerArray[i2].serverIndex;
                                    menuLength++;
                                }
                                menuText1[menuLength] = "Follow";
                                menuText2[menuLength] = "@whi@" + clan + getPrefix(playerArray[i2].group) + playerArray[i2].name + s;
                                menuID[menuLength] = 2820;
                                menuActionType[menuLength] = playerArray[i2].serverIndex;
                                menuLength++;
                            }
                            break;
                        case 2:
                            if(selectedSpell >= 0) {
                                if(Data.spellType[selectedSpell] == 3) {
                                    menuText1[menuLength] = "Cast " + Data.spellName[selectedSpell] + " on";
                                    menuText2[menuLength] = "@lre@" + Data.itemName[groundItemType[i2]];
                                    menuID[menuLength] = 200;
                                    menuActionX[menuLength] = groundItemX[i2];
                                    menuActionY[menuLength] = groundItemY[i2];
                                    menuActionType[menuLength] = groundItemType[i2];
                                    menuActionVariable[menuLength] = selectedSpell;
                                    menuLength++;
                                }
                            } else if(selectedItem >= 0) {
                                menuText1[menuLength] = "Use " + selectedItemName + " with";
                                menuText2[menuLength] = "@lre@" + Data.itemName[groundItemType[i2]];
                                menuID[menuLength] = 210;
                                menuActionX[menuLength] = groundItemX[i2];
                                menuActionY[menuLength] = groundItemY[i2];
                                menuActionType[menuLength] = groundItemType[i2];
                                menuActionVariable[menuLength] = selectedItem;
                                menuLength++;
                            } else {
                                menuText1[menuLength] = "Take";
                                menuText2[menuLength] = "@lre@" + Data.itemName[groundItemType[i2]];
                                menuID[menuLength] = 220;
                                menuActionX[menuLength] = groundItemX[i2];
                                menuActionY[menuLength] = groundItemY[i2];
                                menuActionType[menuLength] = groundItemType[i2];
                                menuLength++;
                                menuText1[menuLength] = "Examine";
                                menuText2[menuLength] = "@lre@" + Data.itemName[groundItemType[i2]] + "(" + groundItemType[i2] + ")";
                                menuID[menuLength] = 3200;
                                menuActionType[menuLength] = groundItemType[i2];
                                menuLength++;
                            }
                            break;
                        case 3:
                            String s1 = "";
                            int l3 = -1;
                            int i4 = npcs[i2].id;
                            if(Data.npcAttackable[i4] > 0) {
                                int j4 = (Data.npcAttack[i4] + Data.npcDefense[i4] + Data.npcStrength[i4] + Data.npcHits[i4]) / 4;
                                int k4 = (playerStatBase[0] + playerStatBase[1] + playerStatBase[2] + playerStatBase[3] + 27) / 4;
                                l3 = k4 - j4;
                                s1 = "@yel@";
                                if(l3 < 0)
                                    s1 = "@or1@";
                                if(l3 < -3)
                                    s1 = "@or2@";
                                if(l3 < -6)
                                    s1 = "@or3@";
                                if(l3 < -9)
                                    s1 = "@red@";
                                if(l3 > 0)
                                    s1 = "@gr1@";
                                if(l3 > 3)
                                    s1 = "@gr2@";
                                if(l3 > 6)
                                    s1 = "@gr3@";
                                if(l3 > 9)
                                    s1 = "@gre@";
                                s1 = " " + s1 + "(level-" + j4 + ")";
                            }
                            if(selectedSpell >= 0) {
                                if(Data.spellType[selectedSpell] == 2) {
                                    menuText1[menuLength] = "Cast " + Data.spellName[selectedSpell] + " on";
                                    menuText2[menuLength] = "@yel@" + Data.npcName[npcs[i2].id];
                                    menuID[menuLength] = 700;
                                    menuActionX[menuLength] = npcs[i2].currentX;
                                    menuActionY[menuLength] = npcs[i2].currentY;
                                    menuActionType[menuLength] = npcs[i2].serverIndex;
                                    menuActionVariable[menuLength] = selectedSpell;
                                    menuLength++;
                                }
                            } else if(selectedItem >= 0) {
                                menuText1[menuLength] = "Use " + selectedItemName + " with";
                                menuText2[menuLength] = "@yel@" + Data.npcName[npcs[i2].id];
                                menuID[menuLength] = 710;
                                menuActionX[menuLength] = npcs[i2].currentX;
                                menuActionY[menuLength] = npcs[i2].currentY;
                                menuActionType[menuLength] = npcs[i2].serverIndex;
                                menuActionVariable[menuLength] = selectedItem;
                                menuLength++;
                            } else {
                                if(Data.npcAttackable[i4] > 0) {
                                    menuText1[menuLength] = "Attack";
                                    menuText2[menuLength] = "@yel@" + Data.npcName[npcs[i2].id] + s1;
                                    if(l3 >= 0)
                                        menuID[menuLength] = 715;
                                    else
                                        menuID[menuLength] = 2715;
                                    menuActionX[menuLength] = npcs[i2].currentX;
                                    menuActionY[menuLength] = npcs[i2].currentY;
                                    menuActionType[menuLength] = npcs[i2].serverIndex;
                                    menuLength++;
                                }
                                menuText1[menuLength] = "Talk-to";
                                menuText2[menuLength] = "@yel@" + Data.npcName[npcs[i2].id];
                                menuID[menuLength] = 720;
                                menuActionX[menuLength] = npcs[i2].currentX;
                                menuActionY[menuLength] = npcs[i2].currentY;
                                menuActionType[menuLength] = npcs[i2].serverIndex;
                                menuLength++;
                                if(!Data.npcCommand[i4].equals("")) {
                                    menuText1[menuLength] = Data.npcCommand[i4];
                                    menuText2[menuLength] = "@yel@" + Data.npcName[npcs[i2].id];
                                    menuID[menuLength] = 725;
                                    menuActionX[menuLength] = npcs[i2].currentX;
                                    menuActionY[menuLength] = npcs[i2].currentY;
                                    menuActionType[menuLength] = npcs[i2].serverIndex;
                                    menuLength++;
                                }
                                if(userGroup == 2) {
                                    menuText1[menuLength] = "Server Info";
                                    menuText2[menuLength] = "@yel@" + Data.npcName[npcs[i2].id];
                                    menuID[menuLength] = 723;
                                    menuActionType[menuLength] = npcs[i2].serverIndex;
                                    menuLength++;
                                }
                                menuText1[menuLength] = "Examine";
                                menuText2[menuLength] = "@yel@" + Data.npcName[npcs[i2].id] + "(" + npcs[i2].id + ")" + (userGroup >= 1 ? "(" + npcs[i2].serverIndex + ")" : "");
                                menuID[menuLength] = 3700;
                                menuActionType[menuLength] = npcs[i2].id;
                                menuLength++;
                            }
                            break;
                        default:
                            break;
                    }
                } else if(model.key >= 10000) {
                    int j2 = model.key - 10000;
                    int i3 = wallObjectType[j2];
                    if(!aBooleanArray970[j2]) {
                        if(selectedSpell >= 0) {
                            if(Data.spellType[selectedSpell] == 4) {
                                menuText1[menuLength] = "Cast " + Data.spellName[selectedSpell] + " on";
                                menuText2[menuLength] = "@cya@" + Data.doorName[i3];
                                menuID[menuLength] = 300;
                                menuActionX[menuLength] = wallObjectX[j2];
                                menuActionY[menuLength] = wallObjectY[j2];
                                menuActionType[menuLength] = wallObjectDirection[j2];
                                menuActionVariable[menuLength] = selectedSpell;
                                menuLength++;
                            }
                        } else if(selectedItem >= 0) {
                            menuText1[menuLength] = "Use " + selectedItemName + " with";
                            menuText2[menuLength] = "@cya@" + Data.doorName[i3];
                            menuID[menuLength] = 310;
                            menuActionX[menuLength] = wallObjectX[j2];
                            menuActionY[menuLength] = wallObjectY[j2];
                            menuActionType[menuLength] = wallObjectDirection[j2];
                            menuActionVariable[menuLength] = selectedItem;
                            menuLength++;
                        } else {
                            if(!Data.doorCommand1[i3].equalsIgnoreCase("WalkTo")) {
                                menuText1[menuLength] = Data.doorCommand1[i3];
                                menuText2[menuLength] = "@cya@" + Data.doorName[i3];
                                menuID[menuLength] = 320;
                                menuActionX[menuLength] = wallObjectX[j2];
                                menuActionY[menuLength] = wallObjectY[j2];
                                menuActionType[menuLength] = wallObjectDirection[j2];
                                menuLength++;
                            }
                            if(!Data.doorCommand2[i3].equalsIgnoreCase("Examine")) {
                                menuText1[menuLength] = Data.doorCommand2[i3];
                                menuText2[menuLength] = "@cya@" + Data.doorName[i3];
                                menuID[menuLength] = 2300;
                                menuActionX[menuLength] = wallObjectX[j2];
                                menuActionY[menuLength] = wallObjectY[j2];
                                menuActionType[menuLength] = wallObjectDirection[j2];
                                menuLength++;
                            }
                            menuText1[menuLength] = "Examine";
                            menuText2[menuLength] = "@cya@" + Data.doorName[i3] + "(" + i3 + ")";
                            menuID[menuLength] = 3300;
                            menuActionType[menuLength] = i3;
                            menuLength++;
                        }
                        aBooleanArray970[j2] = true;
                    }
                } else if(model.key >= 0) {
                    int k2 = model.key;
                    int j3 = objectType[k2];
                    if(!aBooleanArray827[k2]) {
                        if(selectedSpell >= 0) {
                            if(Data.spellType[selectedSpell] == 5) {
                                menuText1[menuLength] = "Cast " + Data.spellName[selectedSpell] + " on";
                                menuText2[menuLength] = "@cya@" + Data.objectName[j3];
                                menuID[menuLength] = 400;
                                menuActionX[menuLength] = objectX[k2];
                                menuActionY[menuLength] = objectY[k2];
                                menuActionType[menuLength] = objectID[k2];
                                menuActionVariable[menuLength] = objectType[k2];
                                menuActionVariable2[menuLength] = selectedSpell;
                                menuLength++;
                            }
                        } else if(selectedItem >= 0) {
                            menuText1[menuLength] = "Use " + selectedItemName + " with";
                            menuText2[menuLength] = "@cya@" + Data.objectName[j3];
                            menuID[menuLength] = 410;
                            menuActionX[menuLength] = objectX[k2];
                            menuActionY[menuLength] = objectY[k2];
                            menuActionType[menuLength] = objectID[k2];
                            menuActionVariable[menuLength] = objectType[k2];
                            menuActionVariable2[menuLength] = selectedItem;
                            menuLength++;
                        } else {
                            if(!Data.objectCommand1[j3].equalsIgnoreCase("WalkTo")) {
                                menuText1[menuLength] = Data.objectCommand1[j3];
                                menuText2[menuLength] = "@cya@" + Data.objectName[j3];
                                menuID[menuLength] = 420;
                                menuActionX[menuLength] = objectX[k2];
                                menuActionY[menuLength] = objectY[k2];
                                menuActionType[menuLength] = objectID[k2];
                                menuActionVariable[menuLength] = objectType[k2];
                                menuLength++;
                            }
                            if(!Data.objectCommand2[j3].equalsIgnoreCase("Examine")) {
                                menuText1[menuLength] = Data.objectCommand2[j3];
                                menuText2[menuLength] = "@cya@" + Data.objectName[j3];
                                menuID[menuLength] = 2400;
                                menuActionX[menuLength] = objectX[k2];
                                menuActionY[menuLength] = objectY[k2];
                                menuActionType[menuLength] = objectID[k2];
                                menuActionVariable[menuLength] = objectType[k2];
                                menuLength++;
                            }
                            if(userGroup >= 1) {
                                menuText1[menuLength] = "Remove";
                                menuText2[menuLength] = "@cya@" + Data.objectName[j3] + "(" + j3 + ", (" + (objectX[k2] + areaX) + ", " + (objectY[k2] + areaY) + "))";
                                menuID[menuLength] = 3403;
                                menuActionX[menuLength] = objectX[k2];
                                menuActionY[menuLength] = objectY[k2];
                                menuActionType[menuLength] = j3;
                                menuLength++;
                            }
                            menuText1[menuLength] = "Examine";
                            menuText2[menuLength] = "@cya@" + Data.objectName[j3] + "(" + j3 + ")";
                            menuID[menuLength] = 3400;
                            menuActionType[menuLength] = j3;
                            menuLength++;
                        }
                        aBooleanArray827[k2] = true;
                    }
                } else {
                    if(k1 >= 0)
                        k1 = model.faceTag[k1] - 0x30d40;
                    if(k1 >= 0)
                        j = k1;
                }
        }
        if(selectedSpell >= 0 && Data.spellType[selectedSpell] <= 1) {
            menuText1[menuLength] = "Cast " + Data.spellName[selectedSpell] + " on self";
            menuText2[menuLength] = "";
            menuID[menuLength] = 1000;
            menuActionType[menuLength] = selectedSpell;
            menuLength++;
        }
        if(j != -1) {
            int l1 = j;
            if(selectedSpell >= 0) {
                if(Data.spellType[selectedSpell] == 6) {
                    menuText1[menuLength] = "Cast " + Data.spellName[selectedSpell] + " on ground";
                    menuText2[menuLength] = "";
                    menuID[menuLength] = 900;
                    menuActionX[menuLength] = world.selectedX[l1];
                    menuActionY[menuLength] = world.selectedY[l1];
                    menuActionType[menuLength] = selectedSpell;
                    menuLength++;
                }
            } else if(selectedItem < 0) {
                menuText1[menuLength] = "Walk here";
                menuText2[menuLength] = "";
                menuID[menuLength] = 920;
                menuActionX[menuLength] = world.selectedX[l1];
                menuActionY[menuLength] = world.selectedY[l1];
                menuLength++;
            }
        }
    }

    private void drawLoggingOutBox() {
        surface.drawBox(gameWidth / 2 - 130, gameHeight / 2 - 30, 260, 60, 0);
        surface.drawBoxEdge(gameWidth / 2 - 130, gameHeight / 2 - 30, 260, 60, 0xffffff);
        surface.drawStringCentered("Logging out...", gameWidth / 2, gameHeight / 2, 5, 0xffffff);
    }

    private void drawLoginScreen() {
        hasReceivedWelcomeBoxDetails = false;
        surface.interlace = false;
        surface.blackScreen();
        if(loginScreenNumber == 0 || loginScreenNumber == 1 || loginScreenNumber == 2 || loginScreenNumber == 3) {
            int i = (loginTimer * 2) % 3072;
            if(i < 1024) {
                surface.drawSprite(0, 10, SPRITE_LOGO);
                if(i > 768)
                    surface.fade(0, 10, SPRITE_LOGO + 1, i - 768);
            } else if(i < 2048) {
                surface.drawSprite(0, 10, SPRITE_LOGO + 1);
                if(i > 1792)
                    surface.fade(0, 10, SPRITE_MEDIA + 10, i - 1792);
            } else {
                surface.drawSprite(0, 10, SPRITE_MEDIA + 10);
                if(i > 2816)
                    surface.fade(0, 10, SPRITE_LOGO, i - 2816);
            }
        }
        if(loginScreenNumber == 0)
            menuWelcome.drawMenu();
        if(loginScreenNumber == 1)
            menuNewUser.drawMenu();
        if(loginScreenNumber == 2)
            menuLogin.drawMenu();
        surface.drawSprite(0, windowHeight, SPRITE_MEDIA + 22);
        surface.draw(aGraphics936, 0, 0);
    }

    private void drawMagicWindow(boolean flag) {
        int i = surface.menuMaxWidth - 199;
        int j = 36;
        surface.drawSprite(i - 49, 3, SPRITE_MEDIA + 4);
        char c = '\304';
        char c1 = '\266';
        int l;
        int k = l = Surface.convertRGBToLong(160, 160, 160);
        if(menuMagicPrayersSelected == 0)
            k = Surface.convertRGBToLong(220, 220, 220);
        else
            l = Surface.convertRGBToLong(220, 220, 220);
        surface.drawBoxAlpha(i, j, c / 2, 24, k, 128);
        surface.drawBoxAlpha(i + c / 2, j, c / 2, 24, l, 128);
        surface.drawBoxAlpha(i, j + 24, c, 90, Surface.convertRGBToLong(220, 220, 220), 128);
        surface.drawBoxAlpha(i, j + 24 + 90, c, c1 - 90 - 24, Surface.convertRGBToLong(160, 160, 160), 128);
        surface.drawLineX(i, j + 24, c, 0);
        surface.drawLineY(i + c / 2, j, 24, 0);
        surface.drawLineX(i, j + 113, c, 0);
        surface.drawStringCentered("Magic", i + c / 4, j + 16, 4, 0);
        surface.drawStringCentered("Prayers", i + c / 4 + c / 2, j + 16, 4, 0);
        if(menuMagicPrayersSelected == 0) {
            spellMenu.resetListTextCount(spellMenuHandle);
            int i1 = 0;
            for(int spellIndex = 0; spellIndex < Data.spellDifferentRuneCount.length; spellIndex++) {
                String s = "@yel@";
                for(int runeIndex = 0; runeIndex < Data.spellDifferentRuneCount[spellIndex]; runeIndex++) {
                    int k4 = Data.spellRequiredRuneID[spellIndex][runeIndex];
                    if(hasRequiredRunes(k4, Data.spellRequiredRuneCount[spellIndex][runeIndex]))
                        continue;
                    s = "@whi@";
                    break;
                }
                int spellLevel = playerStatCurrent[6];
                if(Data.spellRequiredLevel[spellIndex] > spellLevel)
                    s = "@bla@";
                spellMenu.drawMenuListText(spellMenuHandle, i1++, s + "Level " + Data.spellRequiredLevel[spellIndex] + ": " + Data.spellName[spellIndex]);
            }
            spellMenu.drawMenu();
            int selectedSpellIndex = spellMenu.selectedListIndex(spellMenuHandle);
            if(selectedSpellIndex != -1) {
                surface.drawString("Level " + Data.spellRequiredLevel[selectedSpellIndex] + ": " + Data.spellName[selectedSpellIndex], i + 2, j + 124, 1, 0xffff00);
                surface.drawString(Data.spellDescription[selectedSpellIndex], i + 2, j + 136, 0, 0xffffff);
                for(int i4 = 0; i4 < Data.spellDifferentRuneCount[selectedSpellIndex]; i4++) {
                    int runeID = Data.spellRequiredRuneID[selectedSpellIndex][i4];
                    surface.drawSprite(i + 2 + i4 * 44, j + 150, SPRITE_ITEM + Data.itemInventoryPicture[runeID]);
                    int runeInvCount = inventoryCount(runeID);
                    int runeCount = Data.spellRequiredRuneCount[selectedSpellIndex][i4];
                    String s2 = "@red@";
                    if(hasRequiredRunes(runeID, runeCount))
                        s2 = "@gre@";
                    surface.drawString(s2 + runeInvCount + "/" + runeCount, i + 2 + i4 * 44, j + 150, 1, 0xffffff);
                }
            } else {
                surface.drawString("Point at a spell for a description", i + 2, j + 124, 1, 0);
            }
        }
        if(menuMagicPrayersSelected == 1) {
            spellMenu.resetListTextCount(spellMenuHandle);
            int j1 = 0;
            for(int j2 = 0; j2 < Data.prayerName.length; j2++) {
                String s1 = "@whi@";
                if(Data.prayerRequiredLevel[j2] > playerStatBase[5])
                    s1 = "@bla@";
                if(prayerOn[j2])
                    s1 = "@gre@";
                spellMenu.drawMenuListText(spellMenuHandle, j1++, s1 + "Level " + Data.prayerRequiredLevel[j2] + ": " + Data.prayerName[j2]);
            }
            spellMenu.drawMenu();
            int j3 = spellMenu.selectedListIndex(spellMenuHandle);
            if(j3 != -1) {
                surface.drawStringCentered("Level " + Data.prayerRequiredLevel[j3] + ": " + Data.prayerName[j3], i + c / 2, j + 130, 1, 0xffff00);
                surface.drawStringCentered(Data.prayerDescription[j3], i + c / 2, j + 145, 0, 0xffffff);
                surface.drawStringCentered("Drain rate: " + Data.prayerDrainRate[j3], i + c / 2, j + 160, 1, 0);
            } else {
                surface.drawString("Point at a prayer for a description", i + 2, j + 124, 1, 0);
            }
        }
        if(!flag)
            return;
        i = super.mouseX - (surface.menuMaxWidth - 199);
        j = super.mouseY - 36;
        if(i >= 0 && j >= 0 && i < 196 && j < 182) {
            spellMenu.updateActions(i + (surface.menuMaxWidth - 199), j + 36, super.lastMouseDownButton, super.mouseDownButton);
            if(j <= 24 && mouseButtonClick == 1)
                if(i < 98 && menuMagicPrayersSelected == 1) {
                    menuMagicPrayersSelected = 0;
                    prayerMenuIndex = spellMenu.getMenuIndex(spellMenuHandle);
                    spellMenu.method165(spellMenuHandle, magicMenuIndex);
                } else if(i > 98 && menuMagicPrayersSelected == 0) {
                    menuMagicPrayersSelected = 1;
                    magicMenuIndex = spellMenu.getMenuIndex(spellMenuHandle);
                    spellMenu.method165(spellMenuHandle, prayerMenuIndex);
                }
            if(mouseButtonClick == 1 && menuMagicPrayersSelected == 0) {
                int k1 = spellMenu.selectedListIndex(spellMenuHandle);
                if(k1 != -1) {
                    int k2 = playerStatCurrent[6];
                    if(Data.spellRequiredLevel[k1] > k2) {
                        displayMessage("Your magic ability is not high enough for this spell", 3, null);
                    } else {
                        int k3;
                        for(k3 = 0; k3 < Data.spellDifferentRuneCount[k1]; k3++) {
                            int j4 = Data.spellRequiredRuneID[k1][k3];
                            if(hasRequiredRunes(j4, Data.spellRequiredRuneCount[k1][k3]))
                                continue;
                            displayMessage("You don't have all the reagents you need for this spell", 3, null);
                            k3 = -1;
                            break;
                        }
                        if(k3 == Data.spellDifferentRuneCount[k1]) {
                            selectedSpell = k1;
                            autoSpell = k1;
                            selectedItem = -1;
                        }
                    }
                }
            }
            if(mouseButtonClick == 1 && menuMagicPrayersSelected == 1) {
                int l1 = spellMenu.selectedListIndex(spellMenuHandle);
                if(l1 != -1) {
                    int l2 = playerStatBase[5];
                    if(Data.prayerRequiredLevel[l1] > l2)
                        displayMessage("Your prayer ability is not high enough for this prayer", 3, null);
                    else if(playerStatCurrent[5] == 0)
                        displayMessage("You have run out of prayer points. Return to a church to recharge", 3, null);
                    else if(prayerOn[l1]) {
                        super.streamClass.addNewFrame(176);
                        super.streamClass.addByte(l1);
                        super.streamClass.formatCurrentFrame();
                        prayerOn[l1] = false;
                        playSound("prayeroff");
                    } else {
                        super.streamClass.addNewFrame(239);
                        super.streamClass.addByte(l1);
                        super.streamClass.formatCurrentFrame();
                        prayerOn[l1] = true;
                        playSound("prayeron");
                    }
                }
            }
            mouseButtonClick = 0;
        }
    }

    private void drawMapMenu(boolean flag) {
        try {
            int i = surface.menuMaxWidth - 199;
            char c = '\234';
            char c2 = '\230';
            surface.drawSprite(i - 49, 3, SPRITE_MEDIA + 2);
            i += 40;
            surface.drawBox(i, 36, c, c2, 0);
            surface.setBounds(i, 36, i + c, 36 + c2);
            int k = 192 + anInt986;
            int i1 = cameraRotation + anInt985 & 0xff;
            int k1 = ((ourPlayer.currentX - 6040) * 3 * k) / 2048;
            int i3 = ((ourPlayer.currentY - 6040) * 3 * k) / 2048;
            int k4 = Scene.curve[1024 - i1 * 4 & 0x3ff];
            int i5 = Scene.curve[(1024 - i1 * 4 & 0x3ff) + 1024];
            int k5 = i3 * k4 + k1 * i5 >> 18;
            i3 = i3 * i5 - k1 * k4 >> 18;
            k1 = k5;
            surface.drawMinimapFlooring((i + c / 2) - k1, 36 + c2 / 2 + i3, SPRITE_MEDIA - 1, i1 + 64 & 0xff, k);
            for(int i7 = 0; i7 < objectCount; i7++) {
                int l1 = (((objectX[i7] * magicLoc + 64) - ourPlayer.currentX) * 3 * k) / 2048;
                int j3 = (((objectY[i7] * magicLoc + 64) - ourPlayer.currentY) * 3 * k) / 2048;
                int l5 = j3 * k4 + l1 * i5 >> 18;
                j3 = j3 * i5 - l1 * k4 >> 18;
                l1 = l5;
                setPixelsAndAroundColor(i + c / 2 + l1, (36 + c2 / 2) - j3, 65535);
            }
            for(int j7 = 0; j7 < groundItemCount; j7++) {
                int i2 = (((groundItemX[j7] * magicLoc + 64) - ourPlayer.currentX) * 3 * k) / 2048;
                int k3 = (((groundItemY[j7] * magicLoc + 64) - ourPlayer.currentY) * 3 * k) / 2048;
                int i6 = k3 * k4 + i2 * i5 >> 18;
                k3 = k3 * i5 - i2 * k4 >> 18;
                i2 = i6;
                setPixelsAndAroundColor(i + c / 2 + i2, (36 + c2 / 2) - k3, 0xff0000);
            }
            for(int k7 = 0; k7 < npcCount; k7++) {
                Actor mob = npcs[k7];
                int j2 = ((mob.currentX - ourPlayer.currentX) * 3 * k) / 2048;
                int l3 = ((mob.currentY - ourPlayer.currentY) * 3 * k) / 2048;
                int j6 = l3 * k4 + j2 * i5 >> 18;
                l3 = l3 * i5 - j2 * k4 >> 18;
                j2 = j6;
                setPixelsAndAroundColor(i + c / 2 + j2, (36 + c2 / 2) - l3, 0xffff00);
            }
            for(int l7 = 0; l7 < playerCount; l7++) {
                Actor mob_1 = playerArray[l7];
                int k2 = ((mob_1.currentX - ourPlayer.currentX) * 3 * k) / 2048;
                int i4 = ((mob_1.currentY - ourPlayer.currentY) * 3 * k) / 2048;
                int k6 = i4 * k4 + k2 * i5 >> 18;
                i4 = i4 * i5 - k2 * k4 >> 18;
                k2 = k6;
                int j8 = 0xffffff;
                for(int k8 = 0; k8 < super.friendsCount; k8++) {
                    if(mob_1.nameLong != super.friendsListLongs[k8] || super.friendsListOnlineStatus[k8] != 99)
                        continue;
                    j8 = 65280;
                    break;
                }
                setPixelsAndAroundColor(i + c / 2 + k2, (36 + c2 / 2) - i4, j8);
            }
            surface.drawCircle(i + c / 2, 36 + c2 / 2, 2, 0xffffff, 255);
            surface.drawMinimapFlooring(i + 19, 55, SPRITE_MEDIA + 24, cameraRotation + 128 & 0xff, 128);
            surface.setBounds(0, 0, windowWidth, windowHeight + 12);
            if(!flag)
                return;
            i = super.mouseX - (surface.menuMaxWidth - 199);
            int i8 = super.mouseY - 36;
            if(i >= 40 && i8 >= 0 && i < 196 && i8 < 152) {
                char c1 = '\234';
                char c3 = '\230';
                int l = 192 + anInt986;
                int j1 = cameraRotation + anInt985 & 0xff;
                int j = surface.menuMaxWidth - 199;
                j += 40;
                int l2 = ((super.mouseX - (j + c1 / 2)) * 16384) / (3 * l);
                int j4 = ((super.mouseY - (36 + c3 / 2)) * 16384) / (3 * l);
                int l4 = Scene.curve[1024 - j1 * 4 & 0x3ff];
                int j5 = Scene.curve[(1024 - j1 * 4 & 0x3ff) + 1024];
                int l6 = j4 * l4 + l2 * j5 >> 15;
                j4 = j4 * j5 - l2 * l4 >> 15;
                l2 = l6;
                l2 += ourPlayer.currentX;
                j4 = ourPlayer.currentY - j4;
                if(mouseButtonClick == 1)
                    method112(regionX, regionY, l2 / 128, j4 / 128, false);
                mouseButtonClick = 0;
            }
        } catch(NullPointerException e) {
        }
    }

    private void drawOptionsMenu(boolean flag) {
        int i = surface.menuMaxWidth - 199;
        int j = 36;
        surface.drawSprite(i - 49, 3, SPRITE_MEDIA + 6);
        char c = '\304';
        surface.drawBoxAlpha(i, 36, c, 65, Surface.convertRGBToLong(181, 181, 181), 160);
        surface.drawBoxAlpha(i, 101, c, 65, Surface.convertRGBToLong(201, 201, 201), 160);
        surface.drawBoxAlpha(i, 166, c, 95, Surface.convertRGBToLong(181, 181, 181), 160);
        surface.drawBoxAlpha(i, 261, c, 40, Surface.convertRGBToLong(201, 201, 201), 160);
        int k = i + 3;
        int i1 = j + 15;
        surface.drawString("Game options - click to toggle", k, i1, 1, 0);
        i1 += 15;
        if(configAutoCameraAngle)
            surface.drawString("Camera angle mode - @gre@Auto", k, i1, 1, 0xffffff);
        else
            surface.drawString("Camera angle mode - @red@Manual", k, i1, 1, 0xffffff);
        i1 += 15;
        if(configMouseButtons)
            surface.drawString("Mouse buttons - @red@One", k, i1, 1, 0xffffff);
        else
            surface.drawString("Mouse buttons - @gre@Two", k, i1, 1, 0xffffff);
        i1 += 15;
        if(member)
            if(configSoundEffects)
                surface.drawString("Sound effects - @red@off", k, i1, 1, 0xffffff);
            else
                surface.drawString("Sound effects - @gre@on", k, i1, 1, 0xffffff);
        i1 += 15;
        surface.drawString("To change your contact details,", k, i1, 0, 0xffffff);
        i1 += 15;
        surface.drawString("password, recovery questions, etc..", k, i1, 0, 0xffffff);
        i1 += 15;
        surface.drawString("please select 'account management'", k, i1, 0, 0xffffff);
        i1 += 15;
        switch(referId) {
            case 0:
                surface.drawString("from the " + GAME_NAME + " front page", k, i1, 0, 0xffffff);
                break;
            case 1:
                surface.drawString("from the link below the gamewindow", k, i1, 0, 0xffffff);
                break;
            default:
                surface.drawString("from the " + GAME_NAME + " front webpage", k, i1, 0, 0xffffff);
                break;
        }
        i1 += 15;
        i1 += 5;
        surface.drawString("Privacy settings. Will be applied to", i + 3, i1, 1, 0);
        i1 += 15;
        surface.drawString("all people not on your friends list", i + 3, i1, 1, 0);
        i1 += 15;
        if(super.blockChatMessages == 0)
            surface.drawString("Block chat messages: @red@<off>", i + 3, i1, 1, 0xffffff);
        else
            surface.drawString("Block chat messages: @gre@<on>", i + 3, i1, 1, 0xffffff);
        i1 += 15;
        if(super.blockPrivateMessages == 0)
            surface.drawString("Block private messages: @red@<off>", i + 3, i1, 1, 0xffffff);
        else
            surface.drawString("Block private messages: @gre@<on>", i + 3, i1, 1, 0xffffff);
        i1 += 15;
        if(super.blockTradeRequests == 0)
            surface.drawString("Block trade requests: @red@<off>", i + 3, i1, 1, 0xffffff);
        else
            surface.drawString("Block trade requests: @gre@<on>", i + 3, i1, 1, 0xffffff);
        i1 += 15;
        if(member)
            if(super.blockDuelRequests == 0)
                surface.drawString("Block duel requests: @red@<off>", i + 3, i1, 1, 0xffffff);
            else
                surface.drawString("Block duel requests: @gre@<on>", i + 3, i1, 1, 0xffffff);
        i1 += 15;
        i1 += 5;
        surface.drawString("Always logout when you finish", k, i1, 1, 0);
        i1 += 15;
        int k1 = 0xffffff;
        if(super.mouseX > k && super.mouseX < k + c && super.mouseY > i1 - 12 && super.mouseY < i1 + 4)
            k1 = 0xffff00;
        surface.drawString("Click here to logout", i + 3, i1, 1, k1);
        if(!flag)
            return;
        i = super.mouseX - (surface.menuMaxWidth - 199);
        j = super.mouseY - 36;
        if(i >= 0 && j >= 0 && i < 196 && j < 265) {
            int l1 = surface.menuMaxWidth - 199;
            byte byte0 = 36;
            char c1 = '\304';
            int l = l1 + 3;
            int j1 = byte0 + 30;
            if(super.mouseX > l && super.mouseX < l + c1 && super.mouseY > j1 - 12 && super.mouseY < j1 + 4 && mouseButtonClick == 1) {
                configAutoCameraAngle = !configAutoCameraAngle;
                super.streamClass.addNewFrame(247);
                super.streamClass.addByte(0);
                super.streamClass.addByte(configAutoCameraAngle ? 1 : 0);
                super.streamClass.formatCurrentFrame();
            }
            j1 += 15;
            if(super.mouseX > l && super.mouseX < l + c1 && super.mouseY > j1 - 12 && super.mouseY < j1 + 4 && mouseButtonClick == 1) {
                configMouseButtons = !configMouseButtons;
                super.streamClass.addNewFrame(247);
                super.streamClass.addByte(2);
                super.streamClass.addByte(configMouseButtons ? 1 : 0);
                super.streamClass.formatCurrentFrame();
            }
            j1 += 15;
            if(member && super.mouseX > l && super.mouseX < l + c1 && super.mouseY > j1 - 12 && super.mouseY < j1 + 4 && mouseButtonClick == 1) {
                configSoundEffects = !configSoundEffects;
                super.streamClass.addNewFrame(247);
                super.streamClass.addByte(3);
                super.streamClass.addByte(configSoundEffects ? 1 : 0);
                super.streamClass.formatCurrentFrame();
            }
            j1 += 15;
            j1 += 15;
            j1 += 15;
            j1 += 15;
            j1 += 15;
            boolean flag1 = false;
            j1 += 35;
            if(super.mouseX > l && super.mouseX < l + c1 && super.mouseY > j1 - 12 && super.mouseY < j1 + 4 && mouseButtonClick == 1) {
                super.blockChatMessages = 1 - super.blockChatMessages;
                flag1 = true;
            }
            j1 += 15;
            if(super.mouseX > l && super.mouseX < l + c1 && super.mouseY > j1 - 12 && super.mouseY < j1 + 4 && mouseButtonClick == 1) {
                super.blockPrivateMessages = 1 - super.blockPrivateMessages;
                flag1 = true;
            }
            j1 += 15;
            if(super.mouseX > l && super.mouseX < l + c1 && super.mouseY > j1 - 12 && super.mouseY < j1 + 4 && mouseButtonClick == 1) {
                super.blockTradeRequests = 1 - super.blockTradeRequests;
                flag1 = true;
            }
            j1 += 15;
            if(member && super.mouseX > l && super.mouseX < l + c1 && super.mouseY > j1 - 12 && super.mouseY < j1 + 4 && mouseButtonClick == 1) {
                super.blockDuelRequests = 1 - super.blockDuelRequests;
                flag1 = true;
            }
            j1 += 15;
            if(flag1)
                sendUpdatedPrivacyInfo(super.blockChatMessages, super.blockPrivateMessages, super.blockTradeRequests, super.blockDuelRequests);
            j1 += 20;
            if(super.mouseX > l && super.mouseX < l + c1 && super.mouseY > j1 - 12 && super.mouseY < j1 + 4 && mouseButtonClick == 1)
                logout();
            mouseButtonClick = 0;
        }
    }

    private void drawPlayerInfoMenu(boolean flag) {
        int i = surface.menuMaxWidth - 199;
        int j = 36;
        surface.drawSprite(i - 49, 3, SPRITE_MEDIA + 3);
        char c = '\304';
        char c1 = '\u0113';
        // int c1 = 230;
        int l;
        int k = l = Surface.convertRGBToLong(160, 160, 160);
        if(anInt826 == 0)
            k = Surface.convertRGBToLong(220, 220, 220);
        else
            l = Surface.convertRGBToLong(220, 220, 220);
        surface.drawBoxAlpha(i, j, c / 2, 24, k, 128);
        surface.drawBoxAlpha(i + c / 2, j, c / 2, 24, l, 128);
        surface.drawBoxAlpha(i, j + 24, c, c1 - 24, Surface.convertRGBToLong(220, 220, 220), 128);
        surface.drawLineX(i, j + 24, c, 0);
        surface.drawLineY(i + c / 2, j, 24, 0);
        surface.drawStringCentered("Stats", i + c / 4, j + 16, 4, 0);
        surface.drawStringCentered("Info", i + c / 4 + c / 2, j + 16, 4, 0);
        if(anInt826 == 0) {
            int i1 = 72;
            int k1 = -1;
            surface.drawString("Skills", i + 5, i1, 3, 0xffff00);
            i1 += 13;
            for(int k2 = 0; k2 < 9; k2++) {
                int l2 = 0xffffff;
                if(super.mouseX > i + 3 && super.mouseY >= i1 - 11 && super.mouseY < i1 + 2 && super.mouseX < i + 90) {
                    l2 = 0xff0000;
                    k1 = k2;
                }
                surface.drawString(SKILL_NAMES[k2] + ":@yel@" + playerStatCurrent[k2] + "/" + playerStatBase[k2], i + 5, i1, 1, l2);
                l2 = 0xffffff;
                if(super.mouseX >= i + 90 && super.mouseY >= i1 - 13 - 11 && super.mouseY < (i1 - 13) + 2 && super.mouseX < i + 196) {
                    l2 = 0xff0000;
                    k1 = k2 + 9;
                }
                surface.drawString(SKILL_NAMES[k2 + 9] + ":@yel@" + playerStatCurrent[k2 + 9] + "/" + playerStatBase[k2 + 9], (i + c / 2) - 5, i1 - 13, 1, l2);
                i1 += 13;
            }
            surface.drawString("Quest Points:@yel@" + 0, (i + c / 2) - 5, i1 - 13, 1, 0xffffff);
            i1 += 12;
            surface.drawString("Fatigue: @yel@" + (fatigue * 100) / 750 + "%", i + 5, i1 - 13, 1, 0xffffff);
            i1 += 8;
            surface.drawString("Equipment Status", i + 5, i1, 3, 0xffff00);
            i1 += 12;
            for(int j2 = 0; j2 < 3; j2++) {
                surface.drawString(EQUIPMENT_INFO[j2] + ":@yel@" + equipmentStatus[j2], i + 5, i1, 1, 0xffffff);
                if(j2 < 2)
                    surface.drawString(EQUIPMENT_INFO[j2 + 3] + ":@yel@" + equipmentStatus[j2 + 3], i + c / 2 + 25, i1, 1, 0xffffff);
                i1 += 13;
            }
            i1 += 6;
            surface.drawLineX(i, i1 - 15, c, 0);
            if(k1 != -1) {
                surface.drawString(SKILL_NAMES[k1] + " skill", i + 5, i1, 1, 0xffff00);
                i1 += 12;
                long j3 = experienceArray[0];
                for(int i3 = 0; i3 < 98; i3++)
                    if(playerStatExperience[k1] >= experienceArray[i3])
                        j3 = experienceArray[i3 + 1];
                surface.drawString("Total xp: " + playerStatExperience[k1], i + 5, i1, 1, 0xffffff);
                i1 += 12;
                surface.drawString("Next level at: " + j3, i + 5, i1, 1, 0xffffff);
                /*
                 * i1 += 12; surface.drawString("Remaining: " + (j3 -
                 * playerStatExperience[k1]), i + 5, i1, 1, 0xffffff);
                 */
            } else {
                surface.drawString("Overall levels", i + 5, i1, 1, 0xffff00);
                i1 += 12;
                int skillTotal = 0;
                for(int j3 = 0; j3 < 18; j3++)
                    skillTotal += playerStatBase[j3];
                surface.drawString("Skill total: " + skillTotal, i + 5, i1, 1, 0xffffff);
                i1 += 12;
                surface.drawString("Combat level: " + ourPlayer.level, i + 5, i1, 1, 0xffffff);
                i1 += 15;
            }
        }
        if(anInt826 == 1) {
            int i1 = 72; // Player Info
            surface.drawString("Player Info", i + 5, i1, 3, 0xffff00);
            i1 += 13;
            surface.drawString("Username:@yel@ " + getPrefix(ourPlayer.group) + ourPlayer.name, i + 5, i1, 1, 0xffffff);
            i1 += 13;
            surface.drawString((this.ourPlayer.currentX / 128 + this.areaX) + " " + (this.ourPlayer.currentY / 128 + this.areaY) + " " + (this.ourPlayer.currentX / 128 + this.regionX) + " " + (this.ourPlayer.currentY / 128 + this.regionY), i + 5, i1, 1, 0xffffff);
            i1 += 13;
            surface.drawString("Server Index:@yel@ " + ourPlayer.serverIndex, i + 5, i1, 1, 0xffffff);
            i1 += 13;
            surface.drawString("Last IP:@yel@ " + lastLoggedInAddress + " " + this.areaX + " " + this.areaY, i + 5, i1, 1, 0xffffff);
            i1 += 21; // Server Info
            surface.drawString("Server Info", i + 5, i1, 3, 0xffff00);
            i1 += 13;
            surface.drawString("Hostname:@yel@ " + server, i + 5, i1, 1, 0xffffff);
            i1 += 13;
            surface.drawString("Uptime:@yel@ " + timeSince(serverStartTime), i + 5, i1, 1, 0xffffff);
            i1 += 13;
            surface.drawString("Location:@yel@ " + serverLocation, i + 5, i1, 1, 0xffffff);
            i1 += 13;
            surface.drawString("Online:@yel@ " + playersOnline + " users", i + 5, i1, 1, 0xffffff);
            i1 += 13;
        }
        if(!flag)
            return;
        i = super.mouseX - (surface.menuMaxWidth - 199);
        j = super.mouseY - 36;
        if(i >= 0 && j >= 0 && i < c && j < c1) {
            if(j <= 24 && mouseButtonClick == 1) {
                if(i < 98) {
                    anInt826 = 0;
                    return;
                }
                if(i > 98)
                    anInt826 = 1;
            }
        }
    }

    private void drawQuestionMenu() {
        if(mouseButtonClick != 0) {
            for(int i = 0; i < questionMenuCount; i++) {
                if(super.mouseX >= surface.stringWidth(questionMenuAnswer[i], 1) || super.mouseY <= i * 12 || super.mouseY >= 12 + i * 12)
                    continue;
                super.streamClass.addNewFrame(171);
                super.streamClass.addByte(i);
                super.streamClass.formatCurrentFrame();
                break;
            }
            mouseButtonClick = 0;
            showQuestionMenu = false;
            return;
        }
        for(int j = 0; j < questionMenuCount; j++) {
            int k = 65535;
            if(super.mouseX < surface.stringWidth(questionMenuAnswer[j], 1) && super.mouseY > j * 12 && super.mouseY < 12 + j * 12)
                k = 0xff0000;
            surface.drawString(questionMenuAnswer[j], 6, 12 + j * 12, 1, k);
        }
    }

    private void drawRightClickMenu() {
        if(mouseButtonClick != 0) {
            for(int i = 0; i < menuLength; i++) {
                int k = menuX + 2;
                int i1 = menuY + 27 + i * 15;
                if(super.mouseX <= k - 2 || super.mouseY <= i1 - 12 || super.mouseY >= i1 + 4 || super.mouseX >= (k - 3) + menuWidth)
                    continue;
                menuClick(menuIndexes[i]);
                break;
            }
            mouseButtonClick = 0;
            showRightClickMenu = false;
            return;
        }
        if((mouseX < menuX - 10) || (mouseY < menuY - 10) || (mouseX > menuX + menuWidth + 10) || (mouseY > menuY + menuHeight + 10)) {
            showRightClickMenu = false;
            return;
        }
        surface.drawBoxAlpha(menuX, menuY, menuWidth, menuHeight, 0xd0d0d0, 160);
        surface.drawString("Choose option", menuX + 2, menuY + 12, 1, 65535);
        for(int j = 0; j < menuLength; j++) {
            int l = menuX + 2;
            int j1 = menuY + 27 + j * 15;
            int k1 = 0xffffff;
            if(super.mouseX > l - 2 && super.mouseY > j1 - 12 && super.mouseY < j1 + 4 && super.mouseX < (l - 3) + menuWidth)
                k1 = 0xffff00;
            surface.drawString(menuText1[menuIndexes[j]] + " " + menuText2[menuIndexes[j]], l, j1, 1, k1);
        }
    }

    private void drawRightClickOptions() {
        if(!showTradeWindow || !showDuelWindow || !GameUIs.overlay.get(0).isVisible() || !showRightClickMenu || !showPetInventory) {
            if(!showPetInventory)
                if(super.mouseX < tradeWindowX - 10 || super.mouseY < tradeWindowY - 2 || super.mouseX > tradeWindowX + menuWidth + 10 || super.mouseY > tradeWindowY + menuHeight + 10) {
                    tradeWindowX = -100;
                    tradeWindowY = -100;
                    showRightClickMenu = false;
                    rightClickOptions = false;
                    valueSet = false;
                    return;
                }
            for(int i = 0; i < menuLength; i++)
                menuIndexes[i] = i;
            for(boolean flag = false; !flag; ) {
                flag = true;
                for(int j = 0; j < menuLength - 1; j++) {
                    int l = menuIndexes[j];
                    int j1 = menuIndexes[j + 1];
                    if(menuID[l] > menuID[j1]) {
                        menuIndexes[j] = j1;
                        menuIndexes[j + 1] = l;
                        flag = false;
                    }
                }
            }
            menuHeight = (menuLength + 1) * 15;
            menuWidth = surface.stringWidth("Choose option", 1) + 5;
            for(int k1 = 0; k1 < menuLength; k1++) {
                int l1 = surface.stringWidth(menuText1[k1] + " " + menuText2[k1], 1) + 5;
                if(l1 > menuWidth)
                    menuWidth = l1;
            }
            if(!valueSet) {
                tradeWindowX = super.mouseX - menuWidth / 2;
                tradeWindowY = super.mouseY - 7;
                valueSet = true;
            }
            if(tradeWindowX < 0)
                tradeWindowX = 5;
            if(tradeWindowY < 0)
                tradeWindowY = 5;
            if(tradeWindowX + menuWidth > windowWidth)
                tradeWindowX = windowWidth - menuWidth - 5;
            if(tradeWindowY + menuHeight > windowHeight)
                tradeWindowY = windowHeight - menuHeight - 5;
            surface.drawBoxAlpha(tradeWindowX, tradeWindowY, menuWidth, menuHeight, 0xd0d0d0, 160);
            surface.drawString("Choose option", tradeWindowX + 2, tradeWindowY + 12, 1, 65535);
            for(int j = 0; j < menuLength; j++) {
                int l = tradeWindowX + 2;
                int j1 = tradeWindowY + 27 + j * 15;
                int k1 = 0xffffff;
                if(super.mouseX > l - 2 && super.mouseY > j1 - 12 && super.mouseY < j1 + 4 && super.mouseX < (l - 3) + menuWidth)
                    k1 = 0xffff00;
                surface.drawString(menuText1[menuIndexes[j]] + " " + menuText2[menuIndexes[j]], l, j1, 1, k1);
            }
        } else {
            tradeWindowX = -100;
            tradeWindowY = -100;
            for(int jx = 0; jx < menuLength; jx++) {
                menuText1[jx] = null;
                menuText2[jx] = null;
                menuActionVariable[jx] = -1;
                menuActionVariable2[jx] = -1;
                menuID[jx] = -1;
            }
            showRightClickMenu = false;
            rightClickOptions = false;
            valueSet = false;
            inputBoxType = 0;
        }
        if(mouseButtonClick == 1 && rightClickOptions) {
            for(int ix = 0; ix < menuLength; ix++) {
                int k = tradeWindowX + 2;
                int i1 = tradeWindowY + 11 + (ix + 1) * 15;
                if(super.mouseX <= k - 2 || super.mouseY <= i1 - 12 || super.mouseY >= i1 + 4 || super.mouseX >= (k - 3) + menuWidth)
                    continue;
                menuClick(ix);
            }
            tradeWindowX = -100;
            tradeWindowY = -100;
            mouseButtonClick = 0;
            rightClickOptions = false;
            valueSet = false;
        }
    }

    private void drawServerMessageBox() {
        char c = '\u0190';
        char c1 = 'd';
        if(serverMessageBoxTop) {
            //	c1 = '\u01C2';
            c1 = '\u012C';
        }
        surface.drawBox(gameWidth / 2 - c / 2, gameHeight / 2 - c1 / '\002', c, c1, 0);
        surface.drawBoxEdge(gameWidth / 2 - c / 2, gameHeight / 2 - c1 / '\002', c, c1, 0xffffff);
        surface.drawBoxTextColor(serverMessage, gameWidth / 2, gameHeight / 2 - c1 / '\002' + 20, 1, 0xffffff, c - 40);
        int j = 0xffffff;
        if((mouseY > gameHeight / 2 + c1 / '\002' - 15) && (mouseY <= gameHeight / 2 + c1 / '\002' - 4) && (mouseX > gameWidth / 2 - 75) && (mouseX < gameWidth / 2 + 75))
            j = 0xff0000;
        surface.drawStringCentered("Click here to close window", gameWidth / 2, gameHeight / 2 + c1 / '\002' - 7, 1, j);
        if(mouseButtonClick == 1) {
            if(j == 0xff0000)
                showServerMessageBox = false;
            if((mouseX < gameWidth / 2 - 200) || (mouseX > gameWidth / 2 + 200) || (mouseY < gameHeight / 2 - 150) || (mouseY > gameHeight / 2 + 150))
                showServerMessageBox = false;
        }
        mouseButtonClick = 0;
    }

    private void drawShopBox() {
        int byte0 = gameWidth / 2 - 204;
        int byte1 = gameHeight / 2 - 123;
        int offsetX = byte0;
        int offsetY = byte1;
        shopActions();
        surface.drawBox(byte0, byte1, 408, 12, 192);
        int l = 0x989898;
        surface.drawBoxAlpha(byte0, byte1 + 12, 408, 17, l, 160);
        surface.drawBoxAlpha(byte0, byte1 + 29, 8, 170, l, 160);
        surface.drawBoxAlpha(byte0 + 399, byte1 + 29, 9, 170, l, 160);
        surface.drawBoxAlpha(byte0, byte1 + 199, 408, 47, l, 160);
        surface.drawString("Buying and selling items", byte0 + 1, byte1 + 10, 1, 0xffffff);
        int j1 = 0xffffff;
        if(super.mouseX > byte0 + 320 && super.mouseY >= byte1 && super.mouseX < byte0 + 408 && super.mouseY < byte1 + 12)
            j1 = 0xff0000;
        surface.drawBoxTextRight("Close window", byte0 + 406, byte1 + 10, 1, j1);
        surface.drawString("Shops stock in green", byte0 + 2, byte1 + 24, 1, 65280);
        surface.drawString("Number you own in blue", byte0 + 135, byte1 + 24, 1, 65535);
        surface.drawString("Your money: " + insertCommas("" + inventoryCount(10)) + " @yel@gp", byte0 + 280, byte1 + 24, 1, 0xffff00);
        int k2 = 0xd0d0d0;
        int k3 = 0;
        for(int k4 = 0; k4 < 5; k4++) {
            for(int l4 = 0; l4 < 8; l4++) {
                int j5 = byte0 + 7 + l4 * 49;
                int i6 = byte1 + 28 + k4 * 34;
                if(selectedShopItemIndex == k3)
                    surface.drawBoxAlpha(j5, i6, 49, 34, 0xff0000, 160);
                else
                    surface.drawBoxAlpha(j5, i6, 49, 34, k2, 160);
                surface.drawBoxEdge(j5, i6, 50, 35, 0);
                if(shopItems[k3] != -1) {
                    int mask = shopItems[k3] == 183 ? ourPlayer.cape : Data.itemPictureMask[shopItems[k3]];
                    surface.spriteClip4(j5, i6, 48, 32, SPRITE_ITEM + Data.itemInventoryPicture[shopItems[k3]], mask, 0, 0, false);
                    if(shopItemCount[k3] != 65535)
                        surface.drawString(insertCommas(String.valueOf(shopItemCount[k3])), j5 + 1, i6 + 10, 1, 65280);
                    surface.drawBoxTextRight(insertCommas(String.valueOf(inventoryCount(shopItems[k3]))), j5 + 47, i6 + 10, 1, 65535);
                }
                k3++;
            }
        }
        surface.drawLineX(byte0 + 5, byte1 + 222, 398, 0);
        if(selectedShopItemIndex == -1) {
            surface.drawStringCentered("Select an object to buy or sell", byte0 + 204, byte1 + 214, 3, 0xffff00);
            return;
        }
        int i5 = shopItems[selectedShopItemIndex];
        if(i5 != -1) {
            if(shopItemCount[selectedShopItemIndex] > 0) {
                int j6 = shopItemsBuyPrice[selectedShopItemIndex];
                surface.drawString("Buy " + Data.itemName[i5] + " for " + j6 + "gp", offsetX + 2, offsetY + 214, 1, 0xffff00);
                int l3 = 0xffffff;
                if(super.mouseX >= offsetX + 220 && super.mouseY >= offsetY + 204 && super.mouseX < offsetX + 250 && super.mouseY <= offsetY + 215)
                    l3 = 0xff0000;
                surface.drawString("One", offsetX + 222, offsetY + 214, 1, l3);
                if(Data.itemStackable[i5] == 0) {
                    int l7 = shopItemCount[selectedShopItemIndex];
                    if(l7 >= 50) {
                        int i4 = 0xffffff;
                        if(super.mouseX >= offsetX + 250 && super.mouseY >= offsetY + 204 && super.mouseX < offsetX + 280 && super.mouseY <= offsetY + 215)
                            i4 = 0xff0000;
                        surface.drawString("50", offsetX + 252, offsetY + 214, 1, i4);
                    }
                    if(l7 >= 100) {
                        int j4 = 0xffffff;
                        if(super.mouseX >= offsetX + 280 && super.mouseY >= offsetY + 204 && super.mouseX < offsetX + 305 && super.mouseY <= offsetY + 215)
                            j4 = 0xff0000;
                        surface.drawString("100", offsetX + 282, offsetY + 214, 1, j4);
                    }
                    if(l7 >= 500) {
                        int k4 = 0xffffff;
                        if(super.mouseX >= offsetX + 305 && super.mouseY >= offsetY + 204 && super.mouseX < offsetX + 335 && super.mouseY <= offsetY + 215)
                            k4 = 0xff0000;
                        surface.drawString("500", offsetX + 307, offsetY + 214, 1, k4);
                        k4 = 0xffffff;
                        if(super.mouseX >= offsetX + 335 && super.mouseY >= offsetY + 204 && super.mouseX < offsetX + 368 && super.mouseY <= offsetY + 215)
                            k4 = 0xff0000;
                        surface.drawString("X", offsetX + 337, offsetY + 214, 1, k4);
                    }
                }
            } else {
                surface.drawStringCentered("This item is not currently available to buy", byte0 + 204, byte1 + 214, 3, 0xffff00);
            }
            if(inventoryCount(i5) > 0) {
                int k6 = shopItemsSellPrice[selectedShopItemIndex];
                surface.drawString("Sell " + Data.itemName[i5] + " for " + k6 + "gp", offsetX + 2, offsetY + 239, 1, 0xffff00);
                int l3 = 0xffffff;
                if(super.mouseX >= offsetX + 220 && super.mouseY >= byte1 + 229 && super.mouseX < offsetX + 250 && super.mouseY <= byte1 + 240)
                    l3 = 0xff0000;
                surface.drawString("One", offsetX + 222, offsetY + 239, 1, l3);
                if(Data.itemStackable[i5] == 0) {
                    if(inventoryCount(i5) >= 50) {
                        int i4 = 0xffffff;
                        if(super.mouseX >= offsetX + 250 && super.mouseY >= byte1 + 229 && super.mouseX < offsetX + 280 && super.mouseY <= byte1 + 240)
                            i4 = 0xff0000;
                        surface.drawString("50", offsetX + 252, offsetY + 239, 1, i4);
                    }
                    if(inventoryCount(i5) >= 100) {
                        int j4 = 0xffffff;
                        if(super.mouseX >= offsetX + 280 && super.mouseY >= byte1 + 229 && super.mouseX < offsetX + 305 && super.mouseY <= byte1 + 240)
                            j4 = 0xff0000;
                        surface.drawString("100", offsetX + 282, offsetY + 239, 1, j4);
                    }
                    if(inventoryCount(i5) >= 500) {
                        int k4 = 0xffffff;
                        if(super.mouseX >= offsetX + 305 && super.mouseY >= byte1 + 229 && super.mouseX < offsetX + 335 && super.mouseY <= byte1 + 240)
                            k4 = 0xff0000;
                        surface.drawString("500", offsetX + 312, offsetY + 239, 1, k4);
                    }
                    int k4 = 0xffffff;
                    if(super.mouseX >= offsetX + 335 && super.mouseY >= byte1 + 229 && super.mouseX < offsetX + 368 && super.mouseY <= byte1 + 240)
                        k4 = 0xff0000;
                    surface.drawString("X", offsetX + 337, offsetY + 239, 1, k4);
                }
                return;
            }
            surface.drawStringCentered("You do not have any of this item to sell", byte0 + 204, byte1 + 239, 3, 0xffff00);
        }
    }

    private void drawTradeConfirmWindow() {
        int byte0 = gameWidth / 2 - 234;
        int byte1 = gameHeight - (gameHeight / 2 + 131);
        surface.drawBox(byte0, byte1, 468, 16, 192);
        int i = 0x989898;
        surface.drawBoxAlpha(byte0, byte1 + 16, 468, 246, i, 160);
        surface.drawStringCentered("Please confirm your trade with @yel@" + Utility.base37Decode(tradeConfirmOtherNameLong), byte0 + 234, byte1 + 12, 1, 0xffffff);
        surface.drawStringCentered("You are about to give:", byte0 + 117, byte1 + 30, 1, 0xffff00);
        for(int j = 0; j < tradeConfirmItemCount; j++) {
            String s = Data.itemName[tradeConfirmItems[j]];
            if(Data.itemStackable[tradeConfirmItems[j]] == 0)
                s = s + " x " + itemAmountToString(tradeConfirmItemsCount[j]);
            surface.drawStringCentered(s, byte0 + 117, byte1 + 42 + j * 12, 1, 0xffffff);
        }
        if(tradeConfirmItemCount == 0)
            surface.drawStringCentered("Nothing!", byte0 + 117, byte1 + 42, 1, 0xffffff);
        surface.drawStringCentered("In return you will receive:", byte0 + 351, byte1 + 30, 1, 0xffff00);
        for(int k = 0; k < tradeConfirmOtherItemCount; k++) {
            String s1 = Data.itemName[tradeConfirmOtherItems[k]];
            if(Data.itemStackable[tradeConfirmOtherItems[k]] == 0)
                s1 = s1 + " x " + itemAmountToString(tradeConfirmOtherItemsCount[k]);
            surface.drawStringCentered(s1, byte0 + 351, byte1 + 42 + k * 12, 1, 0xffffff);
        }
        if(tradeConfirmOtherItemCount == 0)
            surface.drawStringCentered("Nothing!", byte0 + 351, byte1 + 42, 1, 0xffffff);
        surface.drawStringCentered("Are you sure you want to do this?", byte0 + 234, byte1 + 200, 4, 65535);
        surface.drawStringCentered("There is NO WAY to reverse a trade if you change your mind.", byte0 + 234, byte1 + 215, 1, 0xffffff);
        surface.drawStringCentered("Remember that not all players are trustworthy", byte0 + 234, byte1 + 230, 1, 0xffffff);
        if(!tradeConfirmAccepted) {
            surface.drawSprite((byte0 + 118) - 35, byte1 + 238, SPRITE_MEDIA + 25);
            surface.drawSprite((byte0 + 352) - 35, byte1 + 238, SPRITE_MEDIA + 26);
        } else {
            surface.drawStringCentered("Waiting for other player...", byte0 + 234, byte1 + 250, 1, 0xffff00);
        }
        if(mouseButtonClick == 1) {
            if(super.mouseX < byte0 || super.mouseY < byte1 || super.mouseX > byte0 + 468 || super.mouseY > byte1 + 262) {
                showTradeConfirmWindow = false;
                super.streamClass.addNewFrame(18);
                super.streamClass.formatCurrentFrame();
            }
            if(super.mouseX >= (byte0 + 118) - 35 && super.mouseX <= byte0 + 118 + 70 && super.mouseY >= byte1 + 238 && super.mouseY <= byte1 + 238 + 21) {
                tradeConfirmAccepted = true;
                super.streamClass.addNewFrame(185);
                super.streamClass.formatCurrentFrame();
            }
            if(super.mouseX >= (byte0 + 352) - 35 && super.mouseX <= byte0 + 353 + 70 && super.mouseY >= byte1 + 238 && super.mouseY <= byte1 + 238 + 21) {
                showTradeConfirmWindow = false;
                super.streamClass.addNewFrame(18);
                super.streamClass.formatCurrentFrame();
            }
            mouseButtonClick = 0;
        }
    }

    private void drawTradeWindow() {
        if(clickScreenSend) {
            mouseButtonClick = 4;
            clickScreenSend = false;
        }
        int i = mouseX - (gameWidth - (gameWidth / 2 + 234));
        int j = mouseY - (gameHeight / 2 - 139);
        if(System.currentTimeMillis() - lastTradeDuelUpdate > 50) {
            allowSendCommand = !(inputBoxType > 3 && inputBoxType < 10);
            if(allowSendCommand)
                if(mouseButtonClick != 0 && itemIncrement == 0)
                    itemIncrement = 1;
            if(itemIncrement > 0) {
                if(i >= 0 && j >= 0 && i < 468 && j < 262) {
                    if(i > 216 && j > 30 && i < 462 && j < 235) {
                        int k = (i - 217) / 49 + ((j - 31) / 34) * 5;
                        if(k >= 0 && k < inventoryCount) {
                            boolean flag = false;
                            int l1 = 0;
                            int k2 = inventoryItems[k];
                            if(mouseButtonClick != 2 && !rightClickOptions && mouseButtonClick != 4) {
                                for(int k3 = 0; k3 < tradeMyItemCount; k3++)
                                    if(tradeMyItems[k3] == k2)
                                        if(Data.itemStackable[k2] == 0) {
                                            for(int i4 = 0; i4 < itemIncrement; i4++) {
                                                if(tradeMyItemsCount[k3] < inventoryItemsCount[k])
                                                    tradeMyItemsCount[k3]++;
                                                flag = true;
                                            }
                                        } else
                                            l1++;
                            } else if(mouseButtonClick == 2) {
                                if(rightClickOptions) {
                                    tradeWindowX = -100;
                                    tradeWindowY = -100;
                                    mouseButtonClick = 0;
                                    rightClickOptions = false;
                                    valueSet = false;
                                } else if(!rightClickOptions) {
                                    tradeWindowX = super.mouseX;
                                    tradeWindowY = super.mouseY;
                                    for(int jx = 0; jx < menuLength; jx++) {
                                        menuText1[jx] = null;
                                        menuText2[jx] = null;
                                        menuActionVariable[jx] = -1;
                                        menuActionVariable2[jx] = -1;
                                        menuID[jx] = -1;
                                    }
                                    String name = Data.itemName[k2];
                                    menuLength = 0;
                                    menuText1[menuLength] = "Offer 1 @lre@";
                                    menuText2[menuLength] = name;
                                    menuID[menuLength] = 782;
                                    menuActionVariable[menuLength] = k2;
                                    menuActionVariable2[menuLength] = 1;
                                    menuLength++;
                                    menuText1[menuLength] = "Offer 5 @lre@";
                                    menuText2[menuLength] = name;
                                    menuID[menuLength] = 782;
                                    menuActionVariable[menuLength] = k2;
                                    menuActionVariable2[menuLength] = 5;
                                    menuLength++;
                                    menuText1[menuLength] = "Offer 10 @lre@";
                                    menuText2[menuLength] = name;
                                    menuID[menuLength] = 782;
                                    menuActionVariable[menuLength] = k2;
                                    menuActionVariable2[menuLength] = 10;
                                    menuLength++;
                                    menuText1[menuLength] = "Offer All @lre@";
                                    menuText2[menuLength] = name;
                                    menuID[menuLength] = 782;
                                    menuActionVariable[menuLength] = k2;
                                    menuActionVariable2[menuLength] = inventoryCount(k2);
                                    menuLength++;
                                    menuText1[menuLength] = "Offer X @lre@";
                                    menuText2[menuLength] = name;
                                    menuID[menuLength] = 789;
                                    menuActionVariable[menuLength] = k2;
                                    menuLength++;
                                    rightClickOptions = true;
                                }
                            }
                            if(inventoryCount(k2) <= l1)
                                flag = true;
                            if(mouseButtonClick != 2 && !rightClickOptions && mouseButtonClick != 4) {
                                if(!flag && tradeMyItemCount < 12) {
                                    tradeMyItems[tradeMyItemCount] = k2;
                                    tradeMyItemsCount[tradeMyItemCount] = 1;
                                    tradeMyItemCount++;
                                    flag = true;
                                }
                            }
                            if(flag) {
                                if(mouseButtonClick != 2 && !rightClickOptions && mouseButtonClick != 4) {
                                    lastTradeDuelUpdate = System.currentTimeMillis();
                                    super.streamClass.addNewFrame(242);
                                    super.streamClass.addByte(tradeMyItemCount);
                                    for(int j4 = 0; j4 < tradeMyItemCount; j4++) {
                                        super.streamClass.addShort(tradeMyItems[j4]);
                                        super.streamClass.addInt(tradeMyItemsCount[j4]);
                                    }
                                    super.streamClass.formatCurrentFrame();
                                    tradeOtherAccepted = false;
                                    tradeWeAccepted = false;
                                }
                            }
                        }
                    }
                    if(i > 8 && j > 30 && i < 205 && j < 133) {
                        int l = (i - 9) / 49 + ((j - 31) / 34) * 4;
                        if(l >= 0 && l < tradeMyItemCount) {
                            int j1 = tradeMyItems[l];
                            if(mouseButtonClick != 2 && !rightClickOptions) {
                                for(int i2 = 0; i2 < itemIncrement; i2++) {
                                    if(Data.itemStackable[j1] == 0 && tradeMyItemsCount[l] > 1) {
                                        tradeMyItemsCount[l]--;
                                        continue;
                                    }
                                    tradeMyItemCount--;
                                    mouseDownTime = 0;
                                    for(int l2 = l; l2 < tradeMyItemCount; l2++) {
                                        tradeMyItems[l2] = tradeMyItems[l2 + 1];
                                        tradeMyItemsCount[l2] = tradeMyItemsCount[l2 + 1];
                                    }
                                    break;
                                }
                            }
                            if(mouseButtonClick == 2) {
                                if(rightClickOptions) {
                                    tradeWindowX = -100;
                                    tradeWindowY = -100;
                                    mouseButtonClick = 0;
                                    rightClickOptions = false;
                                    valueSet = false;
                                } else if(!rightClickOptions) {
                                    tradeWindowX = super.mouseX;
                                    tradeWindowY = super.mouseY;
                                    for(int jx = 0; jx < menuLength; jx++) {
                                        menuText1[jx] = null;
                                        menuText2[jx] = null;
                                        menuActionVariable[jx] = -1;
                                        menuActionVariable2[jx] = -1;
                                        menuID[jx] = -1;
                                    }
                                    String name = Data.itemName[j1];
                                    menuLength = 0;
                                    menuText1[menuLength] = "Remove 1 @lre@";
                                    menuText2[menuLength] = name;
                                    menuID[menuLength] = 783;
                                    menuActionVariable[menuLength] = j1;
                                    menuActionVariable2[menuLength] = 1;
                                    menuLength++;
                                    menuText1[menuLength] = "Remove 5 @lre@";
                                    menuText2[menuLength] = name;
                                    menuID[menuLength] = 783;
                                    menuActionVariable[menuLength] = j1;
                                    menuActionVariable2[menuLength] = 5;
                                    menuLength++;
                                    menuText1[menuLength] = "Remove 10 @lre@";
                                    menuText2[menuLength] = name;
                                    menuID[menuLength] = 783;
                                    menuActionVariable[menuLength] = j1;
                                    menuActionVariable2[menuLength] = 10;
                                    menuLength++;
                                    menuText1[menuLength] = "Remove All @lre@";
                                    menuText2[menuLength] = name;
                                    menuID[menuLength] = 783;
                                    menuActionVariable[menuLength] = j1;
                                    menuActionVariable2[menuLength] = 0;
                                    menuActionType[menuLength] = 1234;
                                    menuLength++;
                                    menuText1[menuLength] = "Remove X @lre@";
                                    menuText2[menuLength] = name;
                                    menuID[menuLength] = 881;
                                    menuActionVariable[menuLength] = j1;
                                    menuActionVariable2[menuLength] = -1;
                                    menuLength++;
                                    rightClickOptions = true;
                                }
                            } else if(mouseButtonClick == 1 && !rightClickOptions && mouseButtonClick != 4) {
                                lastTradeDuelUpdate = System.currentTimeMillis();
                                super.streamClass.addNewFrame(242);
                                super.streamClass.addByte(tradeMyItemCount);
                                for(int i3 = 0; i3 < tradeMyItemCount; i3++) {
                                    super.streamClass.addShort(tradeMyItems[i3]);
                                    super.streamClass.addInt(tradeMyItemsCount[i3]);
                                }
                                super.streamClass.formatCurrentFrame();
                                tradeOtherAccepted = false;
                                tradeWeAccepted = false;
                            }
                        }
                    }
                    if(i >= 217 && j >= 238 && i <= 286 && j <= 259 && !rightClickOptions) {
                        lastTradeDuelUpdate = System.currentTimeMillis();
                        tradeWeAccepted = true;
                        super.streamClass.addNewFrame(105);
                        super.streamClass.formatCurrentFrame();
                    }
                    if(i >= 394 && j >= 238 && i < 463 && j < 259 && !rightClickOptions) {
                        showTradeWindow = false;
                        super.streamClass.addNewFrame(18);
                        super.streamClass.formatCurrentFrame();
                    }
                } else if(mouseButtonClick != 2 && !rightClickOptions) {
                    showTradeWindow = false;
                    super.streamClass.addNewFrame(18);
                    super.streamClass.formatCurrentFrame();
                }
                if(mouseButtonClick == 1 && rightClickOptions) {
                    for(int ix = 0; ix < menuLength; ix++) {
                        int k = tradeWindowX + 2;
                        int i1 = tradeWindowY + 11 + (ix + 1) * 15;
                        if(super.mouseX <= k - 2 || super.mouseY <= i1 - 12 || super.mouseY >= i1 + 4 || super.mouseX >= (k - 3) + menuWidth)
                            continue;
                        menuClick(ix);
                    }
                    tradeWindowX = -100;
                    tradeWindowY = -100;
                    mouseButtonClick = 0;
                    rightClickOptions = false;
                    valueSet = false;
                }
                mouseButtonClick = 0;
                itemIncrement = 0;
            }
        }
        if(!showTradeWindow)
            return;
        int byte0 = windowWidth / 2 - 234;
        int byte1 = windowHeight / 2 - 140;
        surface.drawBox(byte0, byte1, 468, 12, 192);
        int i1 = 0x989898;
        surface.drawBoxAlpha(byte0, byte1 + 12, 468, 18, i1, 160);
        surface.drawBoxAlpha(byte0, byte1 + 30, 8, 248, i1, 160);
        surface.drawBoxAlpha(byte0 + 205, byte1 + 30, 11, 248, i1, 160);
        surface.drawBoxAlpha(byte0 + 462, byte1 + 30, 6, 248, i1, 160);
        surface.drawBoxAlpha(byte0 + 8, byte1 + 133, 197, 22, i1, 160);
        surface.drawBoxAlpha(byte0 + 8, byte1 + 258, 197, 20, i1, 160);
        surface.drawBoxAlpha(byte0 + 216, byte1 + 235, 246, 43, i1, 160);
        int k1 = 0xd0d0d0;
        surface.drawBoxAlpha(byte0 + 8, byte1 + 30, 197, 103, k1, 160);
        surface.drawBoxAlpha(byte0 + 8, byte1 + 155, 197, 103, k1, 160);
        surface.drawBoxAlpha(byte0 + 216, byte1 + 30, 246, 205, k1, 160);
        for(int j2 = 0; j2 < 4; j2++)
            surface.drawLineX(byte0 + 8, byte1 + 30 + j2 * 34, 197, 0);
        for(int j3 = 0; j3 < 4; j3++)
            surface.drawLineX(byte0 + 8, byte1 + 155 + j3 * 34, 197, 0);
        for(int l3 = 0; l3 < 7; l3++)
            surface.drawLineX(byte0 + 216, byte1 + 30 + l3 * 34, 246, 0);
        for(int k4 = 0; k4 < 6; k4++) {
            if(k4 < 5)
                surface.drawLineY(byte0 + 8 + k4 * 49, byte1 + 30, 103, 0);
            if(k4 < 5)
                surface.drawLineY(byte0 + 8 + k4 * 49, byte1 + 155, 103, 0);
            surface.drawLineY(byte0 + 216 + k4 * 49, byte1 + 30, 205, 0);
        }
        surface.drawString("Trading with: " + tradeOtherPlayerName, byte0 + 1, byte1 + 10, 1, 0xffffff);
        surface.drawString("Your Offer", byte0 + 9, byte1 + 27, 4, 0xffffff);
        surface.drawString("Opponent's Offer", byte0 + 9, byte1 + 152, 4, 0xffffff);
        surface.drawString("Your Inventory", byte0 + 216, byte1 + 27, 4, 0xffffff);
        if(!tradeWeAccepted)
            surface.drawSprite(byte0 + 217, byte1 + 238, SPRITE_MEDIA + 25);
        surface.drawSprite(byte0 + 394, byte1 + 238, SPRITE_MEDIA + 26);
        if(tradeOtherAccepted) {
            surface.drawStringCentered("Other player", byte0 + 341, byte1 + 246, 1, 0xffffff);
            surface.drawStringCentered("has accepted", byte0 + 341, byte1 + 246 + 10, 1, 0xffffff);
        }
        if(tradeWeAccepted) {
            surface.drawStringCentered("Waiting for", byte0 + 217 + 35, byte1 + 246, 1, 0xffffff);
            surface.drawStringCentered("other player", byte0 + 217 + 35, byte1 + 246 + 10, 1, 0xffffff);
        }
        for(int l4 = 0; l4 < inventoryCount; l4++) {
            int i5 = 217 + byte0 + (l4 % 5) * 49;
            int k5 = 31 + byte1 + (l4 / 5) * 34;
            surface.spriteClip4(i5, k5, 48, 32, SPRITE_ITEM + Data.itemInventoryPicture[inventoryItems[l4]], Data.itemPictureMask[inventoryItems[l4]], 0, 0, false);
            if(Data.itemStackable[inventoryItems[l4]] == 0)
                surface.drawString(insertCommas(String.valueOf(inventoryItemsCount[l4])), i5 + 1, k5 + 10, 1, 0xffff00);
        }
        for(int j5 = 0; j5 < tradeMyItemCount; j5++) {
            int l5 = 9 + byte0 + (j5 % 4) * 49;
            int j6 = 31 + byte1 + (j5 / 4) * 34;
            int mask = tradeMyItems[j5] == 183 ? ourPlayer.cape : Data.itemPictureMask[tradeMyItems[j5]];
            surface.spriteClip4(l5, j6, 48, 32, SPRITE_ITEM + Data.itemInventoryPicture[tradeMyItems[j5]], mask, 0, 0, false);
            if(Data.itemStackable[tradeMyItems[j5]] == 0)
                surface.drawString(insertCommas("" + String.valueOf(tradeMyItemsCount[j5])), l5 + 1, j6 + 10, 1, 0xffff00);
            if(super.mouseX > l5 && super.mouseX < l5 + 48 && super.mouseY > j6 && super.mouseY < j6 + 32)
                surface.drawString(Data.itemName[tradeMyItems[j5]] + ": @whi@" + Data.itemDescription[tradeMyItems[j5]], byte0 + 8, byte1 + 273, 1, 0xffff00);
        }
        for(int i6 = 0; i6 < tradeOtherItemCount; i6++) {
            int k6 = 9 + byte0 + (i6 % 4) * 49;
            int l6 = 156 + byte1 + (i6 / 4) * 34;
            int mask = tradeOtherItems[i6] == 183 ? ourPlayer.cape : Data.itemPictureMask[tradeOtherItems[i6]];
            surface.spriteClip4(k6, l6, 48, 32, SPRITE_ITEM + Data.itemInventoryPicture[tradeOtherItems[i6]], mask, 0, 0, false);
            if(Data.itemStackable[tradeOtherItems[i6]] == 0)
                surface.drawString(insertCommas("" + String.valueOf(tradeOtherItemsCount[i6])), k6 + 1, l6 + 10, 1, 0xffff00);
            if(super.mouseX > k6 && super.mouseX < k6 + 48 && super.mouseY > l6 && super.mouseY < l6 + 32)
                surface.drawString(Data.itemName[tradeOtherItems[i6]] + ": @whi@" + Data.itemDescription[tradeOtherItems[i6]], byte0 + 8, byte1 + 273, 1, 0xffff00);
        }
    }

    private void drawWildernessWarningBox() {
        int i = gameHeight / 2 - 72;
        surface.drawBox(gameWidth / 2 - 170, gameHeight / 2 - 90, 340, 180, 0);
        surface.drawBoxEdge(gameWidth / 2 - 170, gameHeight / 2 - 90, 340, 180, 16777215);
        surface.drawStringCentered("Warning! Proceed with caution", gameWidth / 2, i, 4, 0xff0000);
        i += 26;
        surface.drawStringCentered("If you go much further north you will enter the", gameWidth / 2, i, 1, 0xffffff);
        i += 13;
        surface.drawStringCentered("wilderness. This a very dangerous area where", gameWidth / 2, i, 1, 0xffffff);
        i += 13;
        surface.drawStringCentered("other players can attack you!", gameWidth / 2, i, 1, 0xffffff);
        i += 22;
        surface.drawStringCentered("The further north you go the more dangerous it", gameWidth / 2, i, 1, 0xffffff);
        i += 13;
        surface.drawStringCentered("becomes, but the more treasure you will find.", gameWidth / 2, i, 1, 0xffffff);
        i += 22;
        surface.drawStringCentered("In the wilderness an indicator at the bottom-right", gameWidth / 2, i, 1, 0xffffff);
        i += 13;
        surface.drawStringCentered("of the screen will show the current level of danger", gameWidth / 2, i, 1, 0xffffff);
        i += 22;
        int j = 0xffffff;
        if(super.mouseY > i - 12 && super.mouseY <= i && super.mouseX > gameWidth / 2 - 181 && super.mouseX < gameWidth / 2 + 331)
            j = 0xff0000;
        surface.drawStringCentered("Click here to close window", gameWidth / 2, i, 1, j);
        if(mouseButtonClick != 0) {
            if((mouseY > i - 12) && (mouseY <= i) && (mouseX > gameWidth / 2 - 75) && (mouseX < gameWidth / 2 + 75))
                wildernessType = 2;
            if((mouseX < gameWidth / 2 - 170) || (mouseX > gameWidth / 2 + 170) || (mouseY < gameHeight / 2 - 90) || (mouseY > gameHeight / 2 + 90))
                wildernessType = 2;
            mouseButtonClick = 0;
        }
    }

    public void endRecording() {
        isRecording = false;
    }

    private boolean enginePlayerVisible(int i) {
        int j = ourPlayer.currentX / 128;
        int k = ourPlayer.currentY / 128;
        for(int l = 2; l >= 1; l--) {
            if(i == 1 && ((world.modelAdjacency[j][k - l] & 0x80) == 128 || (world.modelAdjacency[j - l][k] & 0x80) == 128 || (world.modelAdjacency[j - l][k - l] & 0x80) == 128))
                return false;
            if(i == 3 && ((world.modelAdjacency[j][k + l] & 0x80) == 128 || (world.modelAdjacency[j - l][k] & 0x80) == 128 || (world.modelAdjacency[j - l][k + l] & 0x80) == 128))
                return false;
            if(i == 5 && ((world.modelAdjacency[j][k + l] & 0x80) == 128 || (world.modelAdjacency[j + l][k] & 0x80) == 128 || (world.modelAdjacency[j + l][k + l] & 0x80) == 128))
                return false;
            if(i == 7 && ((world.modelAdjacency[j][k - l] & 0x80) == 128 || (world.modelAdjacency[j + l][k] & 0x80) == 128 || (world.modelAdjacency[j + l][k - l] & 0x80) == 128))
                return false;
            if(i == 0 && (world.modelAdjacency[j][k - l] & 0x80) == 128)
                return false;
            if(i == 2 && (world.modelAdjacency[j - l][k] & 0x80) == 128)
                return false;
            if(i == 4 && (world.modelAdjacency[j][k + l] & 0x80) == 128)
                return false;
            if(i == 6 && (world.modelAdjacency[j + l][k] & 0x80) == 128)
                return false;
        }
        return true;
    }

    private void garbageCollect() {
        try {
            if(surface != null) {
                surface.clear();
                surface.pixels = null;
                surface = null;
            }
            if(scene != null) {
                scene.cleanupModels();
                scene = null;
            }
            gameModels = null;
            objectModelArray = null;
            wallObjectModel = null;
            mobArray = null;
            playerArray = null;
            npcRecordArray = null;
            npcs = null;
            ourPlayer = null;
            if(world != null) {
                world.aModelArray596 = null;
                world.aModelArrayArray580 = null;
                world.aModelArrayArray598 = null;
                world.aModel_587 = null;
                world = null;
            }
            System.gc();
        } catch(Exception _ex) {
            _ex.printStackTrace();
        }
    }

    public int getGameHeight() {
        return gameHeight;
    }

    public int getGameWidth() {
        return gameWidth;
    }

    public String getPrefix(int i) {
        switch(i) {
            case 0:
                return "";
            case 1:
                return "@";
            case 2:
                return "~";
        }
        return "";
    }

    public int getSlot(int id) {
        for(int i = 0; i < inventoryItems.length; i++) {
            if(inventoryItems[i] == i) {
                return i;
            }
        }
        return -1;
    }

    public StreamClass getStreamClass() {
        return super.streamClass;
    }

    @Override
    protected final void handleIncomingPacket(int command, int length, byte data[]) {
        for(PacketHandler handler : PACKET_HANDLERS)
            if(handler.getPacketOpcode() == command) {
                handler.handlePacket(this, new ServerPacket(command, data, length));
                return;
            }
        try {
            // System.out.println("\t[p="+command+",len="+length+"]");
            if(command == 103) {
                sleeping = false;
                return;
            }
            if(command == 219) {
                if(!sleeping)
                    sleepFatigue = fatigue;
                sleeping = true;
                super.inputText = "";
                super.enteredText = "";

                // surface.drawSleepingTextPicture(SPRITE_TEXTURE + 1, byteBuffer);
                try {
                    BufferedImage image = ImageIO.read(new ByteArrayInputStream(data, 1, length));
                    captchaWidth = image.getWidth();
                    captchaHeight = image.getHeight();
                    captchaPixels = new int[captchaWidth][captchaHeight];
                    for(int x = 0; x < captchaWidth; x++)
                        for(int y = 0; y < captchaHeight; y++)
                            captchaPixels[x][y] = image.getRGB(x, y);
                } catch(Exception e) {
                    e.printStackTrace();
                }

                sleepScreenMessage = null;
                return;
            }
            if(command == 111) {
                lostConnection();
                return;
            }
            if(command == 132) {
                this.combatStyle = data[1] & 0xFF;
                return;
            }
            if(command == 110) {
                int i = 1;
                if(length - i > 4) {
                    serverStartTime = Utility.getUnsignedLong(data, i);
                    i += 8;
                    /* yourPoints = Utility.getUnsigned4Bytes(byteBuffer, i); */
                    i += 4;
                    playersOnline = Utility.getUnsignedShort(data, i);
                    i += 2;
                    serverLocation = new String(data, i, length - i);
                } else if(length - i == 4) {
                    /* yourPoints = Utility.getUnsigned4Bytes(byteBuffer, i); */
                    i += 4;
                } else {
                    playersOnline = Utility.getUnsignedShort(data, i);
                    i += 2;
                }
                return;
            }
            if(command == 3) { // ping reply
                PING_RECIEVED = System.nanoTime();
                return;
            }
            if(command == 108) {
                for(int l = 1; l < length; )
                    if(Utility.getUnsignedByte(data[l]) == 255) {
                        int newCount = 0;
                        int newSectionX = regionX + data[l + 1] >> 3;
                        int newSectionY = regionY + data[l + 2] >> 3;
                        l += 3;
                        for(int groundItem = 0; groundItem < groundItemCount; groundItem++) {
                            int newX = (groundItemX[groundItem] >> 3) - newSectionX;
                            int newY = (groundItemY[groundItem] >> 3) - newSectionY;
                            if(newX != 0 || newY != 0) {
                                if(groundItem != newCount) {
                                    groundItemX[newCount] = groundItemX[groundItem];
                                    groundItemY[newCount] = groundItemY[groundItem];
                                    groundItemType[newCount] = groundItemType[groundItem];
                                    groundItemObjectVar[newCount] = groundItemObjectVar[groundItem];
                                }
                                newCount++;
                            }
                        }
                        groundItemCount = newCount;
                    } else {
                        int id = Utility.getUnsignedShort(data, l);
                        l += 2;
                        int x = regionX + data[l++];
                        int y = regionY + data[l++];
                        if((id & 0x8000) == 0) { // New Item
                            groundItemX[groundItemCount] = x;
                            groundItemY[groundItemCount] = y;
                            groundItemType[groundItemCount] = id;
                            groundItemObjectVar[groundItemCount] = 0;
                            for(int k23 = 0; k23 < objectCount; k23++) {
                                if(objectX[k23] != x || objectY[k23] != y)
                                    continue;
                                groundItemObjectVar[groundItemCount] = Data.objectGroundItemVar[objectType[k23]];
                                break;
                            }
                            groundItemCount++;
                        } else { // Known Item
                            id &= 0x7fff;
                            int l23 = 0;
                            for(int k26 = 0; k26 < groundItemCount; k26++) {
                                if(groundItemX[k26] != x || groundItemY[k26] != y || groundItemType[k26] != id) {
                                    if(k26 != l23) {
                                        groundItemX[l23] = groundItemX[k26];
                                        groundItemY[l23] = groundItemY[k26];
                                        groundItemType[l23] = groundItemType[k26];
                                        groundItemObjectVar[l23] = groundItemObjectVar[k26];
                                    }
                                    l23++;
                                } else { // Remove
                                    id = -123;
                                }
                            }
                            groundItemCount = l23;
                        }
                    }
                return;
            }
            if(command == 27) {
                for(int offset = 1; offset < length; ) {
                    if(Utility.getUnsignedByte(data[offset]) == 255) {
                        int count = 0;
                        int lX = regionX + data[offset + 1] >> 3;
                        int lY = regionY + data[offset + 2] >> 3;
                        offset += 3;
                        for(int currentObject = 0; currentObject < objectCount; currentObject++) {
                            int sX = (objectX[currentObject] >> 3) - lX;
                            int sY = (objectY[currentObject] >> 3) - lY;
                            if(sX != 0 || sY != 0) {
                                if(currentObject != count) {
                                    objectModelArray[count] = objectModelArray[currentObject];
                                    objectModelArray[count].key = count;
                                    objectX[count] = objectX[currentObject];
                                    objectY[count] = objectY[currentObject];
                                    objectType[count] = objectType[currentObject];
                                    objectID[count] = objectID[currentObject];
                                }
                                count++;
                            } else {
                                scene.removeModel(objectModelArray[currentObject]);
                                world.removeObject(objectX[currentObject], objectY[currentObject], objectType[currentObject], objectID[currentObject]);
                            }
                        }
                        objectCount = count;
                    } else {
                        int id = Utility.getUnsignedShort(data, offset);
                        offset += 2;
                        int x = regionX + data[offset++];
                        int y = regionY + data[offset++];
                        int direction = data[offset++];
                        int count = 0;
                        for(int currentObject = 0; currentObject < objectCount; currentObject++)
                            if(objectX[currentObject] != x || objectY[currentObject] != y || objectID[currentObject] != direction) {
                                if(currentObject != count) {
                                    objectModelArray[count] = objectModelArray[currentObject];
                                    objectModelArray[count].key = count;
                                    objectX[count] = objectX[currentObject];
                                    objectY[count] = objectY[currentObject];
                                    objectType[count] = objectType[currentObject];
                                    objectID[count] = objectID[currentObject];
                                }
                                count++;
                            } else {
                                scene.removeModel(objectModelArray[currentObject]);
                                world.removeObject(objectX[currentObject], objectY[currentObject], objectType[currentObject], objectID[currentObject]);
                            }
                        objectCount = count;
                        if(id != Short.MAX_VALUE) {
                            world.registerObjectDir(x, y, direction);
                            // int l29 = engineHandle.method417(i15, l19);
                            int width;
                            int height;
                            if(direction == 0 || direction == 4) {
                                width = Data.objectWidth[id];
                                height = Data.objectHeight[id];
                            } else {
                                height = Data.objectWidth[id];
                                width = Data.objectHeight[id];
                            }
                            int modelX = ((x + x + width) * magicLoc) / 2;
                            int modelY = ((y + y + height) * magicLoc) / 2;
                            int modelIndex = Data.objectModelIndex[id];
                            GameModel model = gameModels[modelIndex].copy();
                            scene.addModel(model);
                            model.key = objectCount;
                            model.rotate(0, direction * 32, 0);
                            model.translate(modelX, -world.getElevation(modelX, modelY), modelY);
                            model.setLight(true, 48, 48, -50, -10, -50);
                            world.addObject(x, y, id, direction);
                            if(id == 74)
                                model.translate(0, -480, 0);
                            objectX[objectCount] = x;
                            objectY[objectCount] = y;
                            objectType[objectCount] = id;
                            objectID[objectCount] = direction;
                            objectModelArray[objectCount++] = model;
                        }
                    }
                }
                // System.out.println("Recieved "+(length)+" objects.");
                return;
            }
            if(command == 114) {
                int invOffset = 1;
                inventoryCount = data[invOffset++] & 0xff;
                for(int invItem = 0; invItem < inventoryCount; invItem++) {
                    int j15 = Utility.getUnsignedShort(data, invOffset);
                    invOffset += 2;
                    inventoryItems[invItem] = (j15 & 0x7fff);
                    wearing[invItem] = j15 / 32768;
                    if(Data.itemStackable[j15 & 0x7fff] == 0) {
                        int amount = Utility.readInt(data, invOffset);
                        inventoryItemsCount[invItem] = amount;
                        invOffset += 4;
                    } else {
                        inventoryItemsCount[invItem] = 1000;
                    }
                }
                return;
            }
            if(command == 115) {
                int invOffset = 1;
                this.petInventoryCount = data[invOffset++] & 0xff;
                for(int invItem = 0; invItem < 20; invItem++) {
                    if(invItem >= petInventoryCount) {
                        this.petInventoryItem[invItem] = -1;
                        this.petInventoryQunant[invItem] = -1;
                    } else {
                        int j15 = Utility.getUnsignedShort(data, invOffset);
                        invOffset += 2;
                        this.petInventoryItem[invItem] = (j15 & 0x7fff);
                        if(Data.itemStackable[j15 & 0x7fff] == 0) {
                            this.petInventoryQunant[invItem] = Utility.readInt(data, invOffset);
                            invOffset += 4;
                        } else {
                            this.petInventoryQunant[invItem] = 1;
                        }
                    }
                }
                return;
            }
            if(command == 54) {
                int mobUpdateOffset = 1;
                int mobArrayIndex = Utility.getUnsignedShort(data, mobUpdateOffset);
                mobUpdateOffset += 2;
                if(mobArrayIndex < 0 || mobArrayIndex > mobArray.length) {
                    return;
                }
                Actor mob = mobArray[mobArrayIndex];
                mob.mobIntUnknown = Utility.getUnsignedShort(data, mobUpdateOffset);
                mobUpdateOffset += 2;
                mob.nameLong = Utility.getUnsignedLong(data, mobUpdateOffset);
                mobUpdateOffset += 8;
                mob.name = Utility.base37Decode(mob.nameLong);
                int i31 = Utility.getUnsignedByte(data[mobUpdateOffset]);
                mobUpdateOffset++;
                for(int i35 = 0; i35 < i31; i35++) {
                    mob.animationCount[i35] = Utility.getUnsignedByte(data[mobUpdateOffset]);
                    mobUpdateOffset++;
                }
                for(int l37 = i31; l37 < 12; l37++)
                    mob.animationCount[l37] = 0;
                mob.colorHairType = data[mobUpdateOffset++] & 0xff;
                mob.colorTopType = data[mobUpdateOffset++] & 0xff;
                mob.colorBottomType = data[mobUpdateOffset++] & 0xff;
                mob.colorSkinType = data[mobUpdateOffset++] & 0xff;
                mob.level = Utility.getUnsignedShort(data, mobUpdateOffset);
                mobUpdateOffset += 2;
                mob.isSkulled = data[mobUpdateOffset++] & 0xff;
            }
            if(command == 53) {
                int mobCount = Utility.getUnsignedShort(data, 1);
                int mobUpdateOffset = 3;
                for(int currentMob = 0; currentMob < mobCount; currentMob++) {
                    int mobArrayIndex = Utility.getUnsignedShort(data, mobUpdateOffset);
                    mobUpdateOffset += 2;
                    if(mobArrayIndex < 0 || mobArrayIndex > mobArray.length) {
                        return;
                    }
                    Actor mob = mobArray[mobArrayIndex];
                    byte mobUpdateType = data[mobUpdateOffset];
                    mobUpdateOffset++;
                    // System.out.println("Recieving player updates: "+mobArrayIndex+", type="+mobUpdateType);
                    switch(mobUpdateType) {
                        case 0:
                            int i30 = Utility.getUnsignedShort(data, mobUpdateOffset);
                            mobUpdateOffset += 2;
                            if(mob != null) {
                                mob.itemBubbleDelay = 150;
                                mob.itemBubbleId = i30;
                            }
                            break;
                        case 1:
                            // Player talking
                            byte byte7 = data[mobUpdateOffset];
                            mobUpdateOffset++;
                            if(mob != null) {
                                String s2 = ChatMessage.byteToString(data, mobUpdateOffset, byte7);
                                // s2 = ChatFilter.filterString(s2);
                                boolean flag3 = false;
                                for(int k40 = 0; k40 < super.ignoreListCount; k40++)
                                    if(super.ignoreListLongs[k40] == mob.nameLong)
                                        flag3 = true;
                                if(!flag3) {
                                    mob.lastMessageTimeout = 150;
                                    mob.currentMessage = s2;
                                    displayMessage(getPrefix(mob.group) + mob.name + ": " + mob.currentMessage, 2, mob);
                                }
                            }
                            mobUpdateOffset += byte7;
                            break;
                        case 2:
                            // Someone getting hit.
                            int j30 = Utility.getUnsignedShort(data, mobUpdateOffset);
                            mobUpdateOffset += 2;
                            int hits = Utility.getUnsignedShort(data, mobUpdateOffset);
                            mobUpdateOffset += 2;
                            int hitsBase = Utility.getUnsignedShort(data, mobUpdateOffset);
                            mobUpdateOffset += 2;
                            if(mob != null) {
                                mob.currentDamage = j30;
                                mob.hitPointsCurrent = hits;
                                mob.hitPointsBase = hitsBase;
                                mob.healthBarTimer = 200;
                                if(mob == ourPlayer) {
                                    playerStatCurrent[3] = hits;
                                    playerStatBase[3] = hitsBase;
                                    showServerMessageBox = false;
                                }
                            }
                            break;
                        case 3:
                            // Fighting an npc..
                            int k30 = Utility.getUnsignedShort(data, mobUpdateOffset);
                            mobUpdateOffset += 2;
                            int k34 = Utility.getUnsignedShort(data, mobUpdateOffset);
                            mobUpdateOffset += 2;
                            if(mob != null) {
                                mob.attackingCameraInt = k30;
                                mob.attackingNpcIndex = k34;
                                mob.attackingMobIndex = -1;
                                mob.anInt176 = PROJECTILE_SPEED;
                            } else {
                                Actor npc = null;
                                for(int index = 0; index < this.npcCount; index++) {
                                    Actor n = npcs[index];
                                    if(n != null && n.serverIndex == mobArrayIndex) {
                                        npc = n;
                                    }
                                }
                                if(npc != null) {
                                    npc.attackingCameraInt = k30;
                                    npc.attackingNpcIndex = k34;
                                    npc.attackingMobIndex = -1;
                                    npc.anInt176 = PROJECTILE_SPEED;
                                }
                            }
                            break;
                        case 4:
                            // Fighting another player.
                            int l30 = Utility.getUnsignedShort(data, mobUpdateOffset);
                            mobUpdateOffset += 2;
                            int l34 = Utility.getUnsignedShort(data, mobUpdateOffset);
                            mobUpdateOffset += 2;
                            // System.out.println("mob update 4 [target='"+mob.name+"', attacker="+mobArray[l34].name+", attackingCameraInt="+l30+"]");
                            if(mob != null) {
                                mob.attackingCameraInt = l30;
                                mob.attackingMobIndex = l34;
                                mob.attackingNpcIndex = -1;
                                mob.anInt176 = PROJECTILE_SPEED;
                            }
                            break;
                        case 5:
                            // Apperance update
                            if(mob != null) {
                                mob.mobIntUnknown = Utility.getUnsignedShort(data, mobUpdateOffset);
                                mobUpdateOffset += 2;
                                mob.nameLong = Utility.getUnsignedLong(data, mobUpdateOffset);
                                mobUpdateOffset += 8;
                                mob.name = Utility.base37Decode(mob.nameLong);
                                int i31 = Utility.getUnsignedByte(data[mobUpdateOffset]);
                                mobUpdateOffset++;
                                for(int i35 = 0; i35 < i31; i35++) {
                                    mob.animationCount[i35] = Utility.getUnsignedByte(data[mobUpdateOffset]);
                                    mobUpdateOffset++;
                                }
                                for(int l37 = i31; l37 < 12; l37++)
                                    mob.animationCount[l37] = 0;
                                mob.colorHairType = data[mobUpdateOffset++] & 0xff;
                                mob.colorTopType = data[mobUpdateOffset++] & 0xff;
                                mob.colorBottomType = data[mobUpdateOffset++] & 0xff;
                                mob.colorSkinType = data[mobUpdateOffset++] & 0xff;
                                mob.level = Utility.getUnsignedShort(data, mobUpdateOffset);
                                mobUpdateOffset += 2;
                                mob.isSkulled = data[mobUpdateOffset++] & 0xff;
                                mob.group = data[mobUpdateOffset++] & 0xFF;
                            } else {
                                // System.out.println("Recieved a player update for a player we don't know: "+mobArrayIndex);
                                mobUpdateOffset += 14;
                                int j31 = Utility.getUnsignedByte(data[mobUpdateOffset]);
                                mobUpdateOffset += j31 + 1;
                            }
                            break;
                        case 6:
                            // npc talking
                            byte byte8 = data[mobUpdateOffset];
                            mobUpdateOffset++;
                            if(mob != null) {
                                String s3 = ChatMessage.byteToString(data, mobUpdateOffset, byte8);
                                mob.lastMessageTimeout = 150;
                                mob.currentMessage = s3;
                                if(mob == ourPlayer) {
                                    displayMessage(getPrefix(ourPlayer.group) + mob.name + ": " + mob.currentMessage, 5, mob);
                                }
                            }
                            mobUpdateOffset += byte8;
                            break;
                        case 7:
                            // Clan capes
                            long clancape = Utility.getUnsignedLong(data, mobUpdateOffset);
                            mobUpdateOffset += 8;
                            mob.cape = (int) clancape;
                            mob.clanName = Utility.base37Decode(Utility.getUnsignedLong(data, mobUpdateOffset));
                            if(mob.clanName.equals("Null")) {
                                mob.clanName = null;
                            }
                            mobUpdateOffset += 8;
                            break;
                        default:
                            break;
                    }
                }
                return;
            }
            if(command == 95) {
                for(int offset = 1; offset < length; )
                    if(Utility.getUnsignedByte(data[offset]) == 255) {
                        int count = 0;
                        int lX = regionX + data[offset + 1] >> 3;
                        int lY = regionY + data[offset + 2] >> 3;
                        offset += 3;
                        for(int currentWallObject = 0; currentWallObject < wallObjectCount; currentWallObject++) {
                            int sX = (wallObjectX[currentWallObject] >> 3) - lX;
                            int sY = (wallObjectY[currentWallObject] >> 3) - lY;
                            if(sX != 0 || sY != 0) {
                                if(currentWallObject != count) {
                                    wallObjectModel[count] = wallObjectModel[currentWallObject];
                                    wallObjectModel[count].key = count + 10000;
                                    wallObjectX[count] = wallObjectX[currentWallObject];
                                    wallObjectY[count] = wallObjectY[currentWallObject];
                                    wallObjectDirection[count] = wallObjectDirection[currentWallObject];
                                    wallObjectType[count] = wallObjectType[currentWallObject];
                                }
                                count++;
                            } else {
                                scene.removeModel(wallObjectModel[currentWallObject]);
                                world.removeWallObject(wallObjectX[currentWallObject], wallObjectY[currentWallObject], wallObjectDirection[currentWallObject], wallObjectType[currentWallObject]);
                            }
                        }
                        wallObjectCount = count;
                    } else {
                        int id = Utility.getUnsignedShort(data, offset);
                        offset += 2;
                        int x = regionX + data[offset++];
                        int y = regionY + data[offset++];
                        byte direction = data[offset++];
                        int count = 0;
                        for(int currentWallObject = 0; currentWallObject < wallObjectCount; currentWallObject++)
                            if(wallObjectX[currentWallObject] != x || wallObjectY[currentWallObject] != y || wallObjectDirection[currentWallObject] != direction) {
                                if(currentWallObject != count) {
                                    wallObjectModel[count] = wallObjectModel[currentWallObject];
                                    wallObjectModel[count].key = count + 10000;
                                    wallObjectX[count] = wallObjectX[currentWallObject];
                                    wallObjectY[count] = wallObjectY[currentWallObject];
                                    wallObjectDirection[count] = wallObjectDirection[currentWallObject];
                                    wallObjectType[count] = wallObjectType[currentWallObject];
                                }
                                count++;
                            } else {
                                scene.removeModel(wallObjectModel[currentWallObject]);
                                world.removeWallObject(wallObjectX[currentWallObject], wallObjectY[currentWallObject], wallObjectDirection[currentWallObject], wallObjectType[currentWallObject]);
                            }
                        wallObjectCount = count;
                        if(id != Short.MAX_VALUE) {
                            world.setModelAdjacency(x, y, direction, id);
                            GameModel model = addModel(x, y, direction, id, wallObjectCount);
                            wallObjectModel[wallObjectCount] = model;
                            wallObjectX[wallObjectCount] = x;
                            wallObjectY[wallObjectCount] = y;
                            wallObjectType[wallObjectCount] = id;
                            wallObjectDirection[wallObjectCount++] = direction;
                        }
                    }
                return;
            }
            if(command == 190) {
                int size = Utility.getUnsignedShort(data, 1);
                int offset = 3;
                for(int index = 0; index < size; index++) {
                    int currentNpcIndex = Utility.getUnsignedShort(data, offset);
                    offset += 2;
                    Actor currentNpc = npcRecordArray[currentNpcIndex];
                    int updateType = Utility.getUnsignedByte(data[offset]);
                    offset++;
                    if(updateType == 1) { // NPC chat message
                        int targetIndex = Utility.getUnsignedShort(data, offset);
                        offset += 2;
                        byte messageLength = data[offset];
                        offset++;
                        String message = new String(data, offset, messageLength);
                        currentNpc.lastMessageTimeout = 150;
                        currentNpc.currentMessage = message;
                        if(targetIndex == ourPlayer.serverIndex)
                            displayMessage("@yel@" + Data.npcName[currentNpc.id] + ": " + currentNpc.currentMessage, 5, null);
                        offset += messageLength;
                    } else if(updateType == 2) { // NPC hits update
                        int l32 = Utility.getUnsignedShort(data, offset);
                        offset += 2;
                        int i36 = Utility.getUnsignedShort(data, offset);
                        offset += 2;
                        int k38 = Utility.getUnsignedShort(data, offset);
                        offset += 2;
                        currentNpc.currentDamage = l32;
                        currentNpc.hitPointsCurrent = i36;
                        currentNpc.hitPointsBase = k38;
                        currentNpc.healthBarTimer = 200;
                    }
                }
                return;
            }
            if(command == 223) {
                showQuestionMenu = true;
                int newQuestionMenuCount = Utility.getUnsignedByte(data[1]);
                questionMenuCount = newQuestionMenuCount;
                int newQuestionMenuOffset = 2;
                for(int l16 = 0; l16 < newQuestionMenuCount; l16++) {
                    int newQuestionMenuQuestionLength = Utility.getUnsignedByte(data[newQuestionMenuOffset]);
                    newQuestionMenuOffset++;
                    questionMenuAnswer[l16] = new String(data, newQuestionMenuOffset, newQuestionMenuQuestionLength);
                    newQuestionMenuOffset += newQuestionMenuQuestionLength;
                }
                return;
            }
            if(command == 127) {
                showQuestionMenu = false;
                return;
            }
            if(command == 131) {
                notInWilderness = true;
                ourPlayerServerIndex = Utility.getUnsignedShort(data, 1);
                wildX = Utility.getUnsignedShort(data, 3);
                wildY = Utility.getUnsignedShort(data, 5);
                currentPlane = Utility.getUnsignedShort(data, 7); // height
                wildYMultiplier = Utility.getUnsignedShort(data, 9); // 944
                debug("pid=131, y=" + (areaY + regionY) + ", wild=" + wildX + "," + wildY + ", height=" + currentPlane + ", wildYMultiplier=" + wildYMultiplier);
                wildY -= currentPlane * wildYMultiplier;
                debug("new wildY = " + wildY);
            }
            if(command == 180) {
                int l2 = 1;
                for(int k10 = 0; k10 < 18; k10++) {
                    playerStatCurrent[k10] = Utility.getUnsignedShort(data, l2);
                    // System.out.println("Statcurrent["+k10+"] == "+playerStatCurrent[k10]);
                    l2 += 2;
                }
                for(int i17 = 0; i17 < 18; i17++) {
                    playerStatBase[i17] = Utility.getUnsignedShort(data, l2);
                    l2 += 2;
                }
                for(int k21 = 0; k21 < 18; k21++) {
                    playerStatExperience[k21] = Utility.getUnsignedLong(data, l2);
                    l2 += 8;
                }
                return;
            }
            if(command == 177) {
                int i3 = 1;
                for(int x = 0; x < 6; x++) {
                    equipmentStatus[x] = Utility.getSignedShort(data, i3);
                    i3 += 2;
                }
                return;
            }
            if(command == 165) {
                playerAliveTimeout = 250;
                return;
            }
            if(command == 115) {
                int sectionLength = (length - 1) / 4;
                for(int currentSection = 0; currentSection < sectionLength; currentSection++) {
                    int currentSectionX = regionX + Utility.getSignedShort(data, 1 + currentSection * 4) >> 3;
                    int currentSectionY = regionY + Utility.getSignedShort(data, 3 + currentSection * 4) >> 3;
                    int currentCount = 0;
                    for(int currentItem = 0; currentItem < groundItemCount; currentItem++) {
                        int currentItemOffsetX = (groundItemX[currentItem] >> 3) - currentSectionX;
                        int currentItemOffsetY = (groundItemY[currentItem] >> 3) - currentSectionY;
                        if(currentItemOffsetX != 0 || currentItemOffsetY != 0) {
                            if(currentItem != currentCount) {
                                groundItemX[currentCount] = groundItemX[currentItem];
                                groundItemY[currentCount] = groundItemY[currentItem];
                                groundItemType[currentCount] = groundItemType[currentItem];
                                groundItemObjectVar[currentCount] = groundItemObjectVar[currentItem];
                            }
                            currentCount++;
                        }
                    }
                    groundItemCount = currentCount;
                    currentCount = 0;
                    for(int j33 = 0; j33 < objectCount; j33++) {
                        int k36 = (objectX[j33] >> 3) - currentSectionX;
                        int l38 = (objectY[j33] >> 3) - currentSectionY;
                        if(k36 != 0 || l38 != 0) {
                            if(j33 != currentCount) {
                                objectModelArray[currentCount] = objectModelArray[j33];
                                objectModelArray[currentCount].key = currentCount;
                                objectX[currentCount] = objectX[j33];
                                objectY[currentCount] = objectY[j33];
                                objectType[currentCount] = objectType[j33];
                                objectID[currentCount] = objectID[j33];
                            }
                            currentCount++;
                        } else {
                            scene.removeModel(objectModelArray[j33]);
                            world.removeObject(objectX[j33], objectY[j33], objectType[j33], objectID[j33]);
                        }
                    }
                    objectCount = currentCount;
                    currentCount = 0;
                    for(int l36 = 0; l36 < wallObjectCount; l36++) {
                        int i39 = (wallObjectX[l36] >> 3) - currentSectionX;
                        int j41 = (wallObjectY[l36] >> 3) - currentSectionY;
                        if(i39 != 0 || j41 != 0) {
                            if(l36 != currentCount) {
                                wallObjectModel[currentCount] = wallObjectModel[l36];
                                wallObjectModel[currentCount].key = currentCount + 10000;
                                wallObjectX[currentCount] = wallObjectX[l36];
                                wallObjectY[currentCount] = wallObjectY[l36];
                                wallObjectDirection[currentCount] = wallObjectDirection[l36];
                                wallObjectType[currentCount] = wallObjectType[l36];
                            }
                            currentCount++;
                        } else {
                            scene.removeModel(wallObjectModel[l36]);
                            world.removeWallObject(wallObjectX[l36], wallObjectY[l36], wallObjectDirection[l36], wallObjectType[l36]);
                        }
                    }
                    wallObjectCount = currentCount;
                }
                return;
            }
            if(command == 207) {
                showCharacterLookScreen = true;
                return;
            }
            if(command == 4) {
                int currentMob = Utility.getUnsignedShort(data, 1);
                if(mobArray[currentMob] != null) // todo: check what that
                    // mobArray is
                    tradeOtherPlayerName = getPrefix(mobArray[currentMob].group) + mobArray[currentMob].name;
                showTradeWindow = true;
                tradeOtherAccepted = false;
                tradeWeAccepted = false;
                tradeMyItemCount = 0;
                tradeOtherItemCount = 0;
                return;
            }
            if(command == 187) {
                showTradeWindow = false;
                showTradeConfirmWindow = false;
                return;
            }
            if(command == 250) {
                tradeOtherItemCount = data[1] & 0xff;
                int l3 = 2;
                for(int i11 = 0; i11 < tradeOtherItemCount; i11++) {
                    tradeOtherItems[i11] = Utility.getUnsignedShort(data, l3);
                    l3 += 2;
                    tradeOtherItemsCount[i11] = Utility.readInt(data, l3);
                    l3 += 4;
                }
                tradeOtherAccepted = false;
                tradeWeAccepted = false;
                return;
            }
            if(command == 92) {
                byte byte0 = data[1];
                if(byte0 == 1) {
                    tradeOtherAccepted = true;
                    return;
                } else {
                    tradeOtherAccepted = false;
                    return;
                }
            }
            if(command == 253) {
                showShop = true;
                int i4 = 1;
                int j11 = data[i4++] & 0xff;
                byte byte4 = data[i4++];
                shopItemSellPriceModifier = data[i4++] & 0xff;
                shopItemBuyPriceModifier = data[i4++] & 0xff;
                for(int i22 = 0; i22 < 40; i22++)
                    shopItems[i22] = -1;
                for(int j25 = 0; j25 < j11; j25++) {
                    shopItems[j25] = Utility.getUnsignedShort(data, i4);
                    i4 += 2;
                    shopItemCount[j25] = Utility.getUnsignedShort(data, i4);
                    i4 += 2;
                    shopItemsBuyPrice[j25] = Utility.getUnsignedInteger(data, i4);
                    i4 += 4;
                    shopItemsSellPrice[j25] = Utility.getUnsignedInteger(data, i4);
                    i4 += 4;
                }
                if(byte4 == 1) {
                    int l28 = 39;
                    for(int k33 = 0; k33 < inventoryCount; k33++) {
                        if(l28 < j11)
                            break;
                        boolean flag2 = false;
                        for(int j39 = 0; j39 < 40; j39++) {
                            if(shopItems[j39] != inventoryItems[k33])
                                continue;
                            flag2 = true;
                            break;
                        }
                        if(inventoryItems[k33] == 10)
                            flag2 = true;
                        if(!flag2) {
                            shopItems[l28] = inventoryItems[k33] & 0x7fff;
                            shopItemsSellPrice[l28] = Data.itemBasePrice[shopItems[l28]] - (int) (Data.itemBasePrice[shopItems[l28]] / 2.5);
                            shopItemsSellPrice[l28] = shopItemsSellPrice[l28] - (int) (shopItemsSellPrice[l28] * 0.10);
                            shopItemCount[l28] = 0;
                            l28--;
                        }
                    }
                }
                if(selectedShopItemIndex >= 0 && selectedShopItemIndex < 40 && shopItems[selectedShopItemIndex] != selectedShopItemType) {
                    selectedShopItemIndex = -1;
                    selectedShopItemType = -2;
                }
                return;
            }
            if(command == 220) {
                showShop = false;
                return;
            }
            if(command == 18) {
                byte byte1 = data[1];
                if(byte1 == 1) {
                    tradeWeAccepted = true;
                    return;
                } else {
                    tradeWeAccepted = false;
                    return;
                }
            }
            if(command == 152) {
                configAutoCameraAngle = Utility.getUnsignedByte(data[1]) == 1;
                configMouseButtons = Utility.getUnsignedByte(data[2]) == 1;
                configSoundEffects = Utility.getUnsignedByte(data[3]) == 1;
                return;
            }
            if(command == 209) {
                for(int currentPrayer = 0; currentPrayer < length - 1; currentPrayer++) {
                    boolean prayerOff = data[currentPrayer + 1] == 1;
                    if(!prayerOn[currentPrayer] && prayerOff)
                        playSound("prayeron");
                    if(prayerOn[currentPrayer] && !prayerOff)
                        playSound("prayeroff");
                    prayerOn[currentPrayer] = prayerOff;
                }
                return;
            }
            if(command == 93) {
                BankUI bank = (BankUI) GameUIs.overlay.get(0);
                bank.setVisible(true);
                int l4 = 1;
                int newBankSize = data[l4++] & 0xFF;
                for(int bankSlot = newBankSize; bankSlot < bankSize; bankSlot++) {
                    bank.items.put(bankSlot, bank.new BankItem(bankSlot, -1, -1));
                }
                bankSize = newBankSize;
                l4++;
                for(int k11 = 0; k11 < bankSize; k11++) {
                    int id = Utility.getUnsignedShort(data, l4);
                    l4 += 2;
                    int amount = Utility.getUnsignedInteger(data, l4);
                    l4 += 4;
                    bank.items.put(k11, bank.new BankItem(k11, id, amount));
                }
                updateBankItems();
                bank.sortBankItems();
                return;
            }
            if(command == 171) {
                GameUIs.overlay.get(0).setVisible(false);
                return;
            }
            if(command == 211) {
                int i5 = data[1] & 0xff;
                playerStatExperience[i5] = Utility.readInt(data, 2);
                return;
            }
            if(command == 229) {
                int j5 = Utility.getUnsignedShort(data, 1);
                if(mobArray[j5] != null)
                    duelOpponentName = getPrefix(mobArray[j5].group) + mobArray[j5].name;
                showDuelWindow = true;
                duelMyItemCount = 0;
                duelOpponentItemCount = 0;
                duelOpponentAccepted = false;
                duelMyAccepted = false;
                duelNoRetreating = false;
                duelNoMagic = false;
                duelNoPrayer = false;
                duelNoWeapons = false;
                return;
            }
            if(command == 160) {
                showDuelWindow = false;
                showDuelConfirmWindow = false;
                return;
            }
            if(command == 251) {
                showTradeConfirmWindow = true;
                tradeConfirmAccepted = false;
                showTradeWindow = false;
                int k5 = 1;
                tradeConfirmOtherNameLong = Utility.getUnsignedLong(data, k5);
                k5 += 8;
                tradeConfirmOtherItemCount = data[k5++] & 0xff;
                for(int l11 = 0; l11 < tradeConfirmOtherItemCount; l11++) {
                    tradeConfirmOtherItems[l11] = Utility.getUnsignedShort(data, k5);
                    k5 += 2;
                    tradeConfirmOtherItemsCount[l11] = Utility.readInt(data, k5);
                    k5 += 4;
                }
                tradeConfirmItemCount = data[k5++] & 0xff;
                for(int k17 = 0; k17 < tradeConfirmItemCount; k17++) {
                    tradeConfirmItems[k17] = Utility.getUnsignedShort(data, k5);
                    k5 += 2;
                    tradeConfirmItemsCount[k17] = Utility.readInt(data, k5);
                    k5 += 4;
                }
                return;
            }
            if(command == 63) {
                duelOpponentItemCount = data[1] & 0xff;
                int l5 = 2;
                for(int i12 = 0; i12 < duelOpponentItemCount; i12++) {
                    duelOpponentItems[i12] = Utility.getUnsignedShort(data, l5);
                    l5 += 2;
                    duelOpponentItemsCount[i12] = Utility.readInt(data, l5);
                    l5 += 4;
                }
                duelOpponentAccepted = false;
                duelMyAccepted = false;
                return;
            }
            if(command == 198) {
                duelNoRetreating = data[1] == 1;
                duelNoMagic = data[2] == 1;
                duelNoPrayer = data[3] == 1;
                duelNoWeapons = data[4] == 1;
                duelOpponentAccepted = false;
                duelMyAccepted = false;
                return;
            }
            if(command == 139) {
                BankUI bank = (BankUI) GameUIs.overlay.get(0);
                int bankDataOffset = 1;
                int bankSlot = data[bankDataOffset++] & 0xff;
                int bankItemId = Utility.getUnsignedShort(data, bankDataOffset);
                bankDataOffset += 2;
                int bankItemCount = Utility.getUnsignedInteger(data, bankDataOffset);
                bankDataOffset += 4;
                if(bankItemCount == 0) {
                    bank.items.put(bankSlot, bank.new BankItem(bankSlot, -1, -1));
                    for(int i = bankSlot; i < bankSize; i++)
                        if(bank.items.containsKey(i) && bank.items.containsKey(i + 1)) {
                            bank.items.get(i + 1).setPos(i);
                            bank.items.put(i, bank.items.get(i + 1));
                            bank.items.put(i + 1, bank.new BankItem(i + 1, -1, -1));
                        } else
                            bank.items.put(i, bank.new BankItem(i, -1, -1));
                    bankSize--;
                } else {
                    bank.items.put(bankSlot, bank.new BankItem(bankSlot, bankItemId, bankItemCount));
                    if(bankSlot >= bankSize)
                        bankSize = bankSlot + 1;
                }
                updateBankItems();
                bank.sortBankItems();
                return;
            }
            if(command == 228) {
                int j6 = 1;
                int k12 = 1;
                int i18 = data[j6++] & 0xff;
                int k22 = Utility.getUnsignedShort(data, j6);
                j6 += 2;
                if(Data.itemStackable[k22 & 0x7fff] == 0) {
                    k12 = Utility.getSignedInteger(data, j6);
                    if(k12 >= 128)
                        j6 += 4;
                    else
                        j6++;
                }
                inventoryItems[i18] = k22 & 0x7fff;
                wearing[i18] = k22 / 32768;
                inventoryItemsCount[i18] = k12;
                if(i18 >= inventoryCount)
                    inventoryCount = i18 + 1;
                return;
            }
            if(command == 191) {
                int k6 = data[1] & 0xff;
                inventoryCount--;
                for(int l12 = k6; l12 < inventoryCount; l12++) {
                    inventoryItems[l12] = inventoryItems[l12 + 1];
                    inventoryItemsCount[l12] = inventoryItemsCount[l12 + 1];
                    wearing[l12] = wearing[l12 + 1];
                }
                return;
            }
            if(command == 208) {
                int l6 = 1;
                int i13 = data[l6++] & 0xff;
                playerStatCurrent[i13] = Utility.getUnsignedShort(data, l6);
                l6 += 2;
                playerStatBase[i13] = Utility.getUnsignedShort(data, l6);
                l6 += 2;
                playerStatExperience[i13] = Utility.getUnsignedLong(data, l6);
                l6 += 8;
                return;
            }
            if(command == 65) {
                byte byte2 = data[1];
                if(byte2 == 1) {
                    duelOpponentAccepted = true;
                    return;
                } else {
                    duelOpponentAccepted = false;
                    return;
                }
            }
            if(command == 197) {
                byte byte3 = data[1];
                if(byte3 == 1) {
                    duelMyAccepted = true;
                    return;
                } else {
                    duelMyAccepted = false;
                    return;
                }
            }
            if(command == 147) {
                showDuelConfirmWindow = true;
                duelWeAccept = false;
                showDuelWindow = false;
                int i7 = 1;
                duelOpponentNameLong = Utility.getUnsignedLong(data, i7);
                i7 += 8;
                duelConfirmOpponentItemCount = data[i7++] & 0xff;
                for(int j13 = 0; j13 < duelConfirmOpponentItemCount; j13++) {
                    duelConfirmOpponentItems[j13] = Utility.getUnsignedShort(data, i7);
                    i7 += 2;
                    duelConfirmOpponentItemsCount[j13] = Utility.readInt(data, i7);
                    i7 += 4;
                }
                duelConfirmMyItemCount = data[i7++] & 0xff;
                for(int j18 = 0; j18 < duelConfirmMyItemCount; j18++) {
                    duelConfirmMyItems[j18] = Utility.getUnsignedShort(data, i7);
                    i7 += 2;
                    duelConfirmMyItemsCount[j18] = Utility.readInt(data, i7);
                    i7 += 4;
                }
                duelCantRetreat = data[i7++] & 0xff;
                duelUseMagic = data[i7++] & 0xff;
                duelUsePrayer = data[i7++] & 0xff;
                duelUseWeapons = data[i7++] & 0xff;
                return;
            }
            if(command == 11) {
                String s = new String(data, 1, length - 1);
                playSound(s);
                return;
            }
            if(command == 23) {
                if(anInt892 < 50) {
                    int j7 = data[1] & 0xff;
                    int k13 = data[2] + regionX;
                    int k18 = data[3] + regionY;
                    anIntArray782[anInt892] = j7;
                    anIntArray923[anInt892] = 0;
                    anIntArray944[anInt892] = k13;
                    anIntArray757[anInt892] = k18;
                    anInt892++;
                }
                return;
            }
            if(command == 248) {
                if(!hasReceivedWelcomeBoxDetails) {
                    // lastLoggedInDays = DataOperations.getUnsigned2Bytes(byteBuffer,
                    // 1);
                    lastLoggedInAddress = new String(data, 3, length - 3);
                    hasReceivedWelcomeBoxDetails = true;
                }
                return;
            }
            if(command == 148) {
                serverMessage = new String(data, 1, length - 1);
                showServerMessageBox = true;
                serverMessageBoxTop = false;
                return;
            }
            if(command == 64) {
                serverMessage = new String(data, 1, length - 1);
                showServerMessageBox = true;
                serverMessageBoxTop = true;
                return;
            }
            if(command == 172) {
                systemUpdate = Utility.getUnsignedShort(data, 1) * 32;
                // System.out.println("SYS UPDATE: "+systemUpdate);
                return;
            }
            if(command == 15) {
                Utility.getUnsignedByte(data[1]);
            }
        } catch(Exception runtimeexception) {
            runtimeexception.printStackTrace();
        }
    }

    @Override
    protected final void handleMenuKeyDown(int key, char keyChar) {
        if(loggedIn == 0) {
            if(loginScreenNumber == 0)
                menuWelcome.keyDown(key, keyChar);
            if(loginScreenNumber == 1)
                menuNewUser.keyDown(key, keyChar);
            if(loginScreenNumber == 2)
                menuLogin.keyDown(key, keyChar);
        }
        if(loggedIn == 1) {
            /*
             * if(key == 87) { method112(regionX, regionY, this.regionX,
             * regionY-1, false); } else if(key == 65) { method112(regionX,
             * regionY, this.regionX+1, regionY, false); } else if(key == 83) {
             * method112(regionX, regionY, this.regionX, regionY+1, false); }
             * else if(key == 68) { method112(regionX, regionY, this.regionX-1,
             * regionY, false); }
             */
            if(key == 27) {
                if(inputBoxType != 0) {
                    inputBoxType = 0;
                }
                if((showServerMessageBox = true) && (serverMessageBoxTop = true) || (showServerMessageBox = true) && (serverMessageBoxTop = false)) {
                    showServerMessageBox = false;
                    serverMessageBoxTop = false;
                }
                if(showShop) {
                    showShop = false;
                }
                if(GameUIs.overlay.get(0).isVisible()) {
                    super.streamClass.addNewFrame(215);
                    super.streamClass.formatCurrentFrame();
                    GameUIs.overlay.get(0).setVisible(false);
                }
                if(showPetInventory) {
                    showPetInventory = false;
                }
                if(showAbuseWindow != 0) {
                    showAbuseWindow = 0;
                }
            }
            if(key == 114) {
                this.selectedSpell = autoSpell;
            }
            if(key == 40 && (cameraHeight < 3000))
                cameraHeight += 40;
            if(key == 38 && (cameraHeight > 400))
                cameraHeight -= 40;
            if(key == 1002) {
                currentChat--;
                if(currentChat < 0) {
                    currentChat = 0;
                    return;
                }
                gameMenu.updateText(chatInputHandle, messages.get(currentChat));
            }
            if(key == 1003) {
                currentChat++;
                if(currentChat >= messages.size()) {
                    currentChat = messages.size();
                    gameMenu.updateText(chatInputHandle, "");
                } else
                    gameMenu.updateText(chatInputHandle, messages.get(currentChat));
            }
            if(key == 1009)
                showIGPing = !showIGPing;
            for(GraphicalOverlay o : GameUIs.overlay)
                if(o.isVisible())
                    for(GraphicalComponent gc : o.getComponents())
                        if(gc.onKey(keyChar, key))
                            return;
            if(showCharacterLookScreen) {
                characterDesignMenu.keyDown(key, keyChar);
                return;
            }
            if(inputBoxType == 0 && showAbuseWindow == 0 && !sleeping)
                gameMenu.keyDown(key, keyChar);
        }
    }

    @Override
    protected final void handleMouseDown(int button, int x, int y) {
        mouseClickXArray[mouseClickArrayOffset] = x;
        mouseClickYArray[mouseClickArrayOffset] = y;
        mouseClickArrayOffset = mouseClickArrayOffset + 1 & 0x1fff;
        for(int l = 10; l < 4000; l++) {
            int i1 = mouseClickArrayOffset - l & 0x1fff;
            if(mouseClickXArray[i1] == x && mouseClickYArray[i1] == y) {
                boolean flag = false;
                for(int j1 = 1; j1 < l; j1++) {
                    int k1 = mouseClickArrayOffset - j1 & 0x1fff;
                    int l1 = i1 - j1 & 0x1fff;
                    if(mouseClickXArray[l1] != x || mouseClickYArray[l1] != y)
                        flag = true;
                    if(mouseClickXArray[k1] != mouseClickXArray[l1] || mouseClickYArray[k1] != mouseClickYArray[l1])
                        break;
                    if(j1 == l - 1 && flag && lastWalkTimeout == 0 && logoutTimeout == 0) {
                        logout();
                        return;
                    }
                }
            }
        }
    }

    @Override
    protected final void handleScroll(MouseWheelEvent e) {
        int off = e.getWheelRotation();
        if(off > 1)
            off += off;
        else if(off < -1) {
            off -= -off;
        }
        for(GraphicalOverlay overlay : GameUIs.overlay) {
            for(GraphicalComponent gc : overlay.getComponents()) {
                if(gc.getFrameScroll() != null && overlay.onComponent(e.getX(), e.getY(), gc)) {
                    gc.getFrameScroll().scrolling(off < 1 ? 1 : 0);
                }
            }
        }
        if(mouseOverMenu == 5)
            friendsMenu.scroll(friendsMenuHandle, off);
        else if(mouseOverMenu == 4)
            spellMenu.scroll(spellMenuHandle, off);
        else if(messagesTab == 1)
            gameMenu.scroll(generalChatHandle, off);
        else if(messagesTab == 2)
            gameMenu.scroll(questChatHandle, off);
        else if(messagesTab == 3)
            gameMenu.scroll(messagesHandleType6, off);
    }

    @Override
    protected final void handleServerMessage(String s) {
        if(s.startsWith("`")) {
            System.out.println(s.substring(1));
            return;
        }
        if(s.startsWith("@bor@")) {
            displayMessage(s, 4, null);
            return;
        }
        if(s.startsWith("@que@")) {
            displayMessage("@whi@" + s, 5, null);
            return;
        }
        if(s.startsWith("@pri@")) {
            displayMessage(s, 6, null);
        } else {
            displayMessage(s, 3, null);
        }
    }

    private boolean hasRequiredRunes(int i, int j) {
        if(i == 31 && (method117(197) || method117(615) || method117(682)))
            return true;
        if(i == 32 && (method117(102) || method117(616) || method117(683)))
            return true;
        if(i == 33 && (method117(101) || method117(617) || method117(684)))
            return true;
        if(i == 34 && (method117(103) || method117(618) || method117(685)))
            return true;
        if(i == 619 && method117(1289))
            return true;
        return inventoryCount(i) >= j;
    }

    public String insertCommas(String str) {
        String s = String.valueOf(str);
        for(int j = s.length() - 3; j > 0; j -= 3) {
            s = s.substring(0, j) + "," + s.substring(j);
        }
        if(s.length() > 8) {
            s = "@gre@" + s.substring(0, s.length() - 5) + "m";
            s = s.replaceAll(",", "");
            s = s.substring(0, s.length() - 3) + "." + s.substring(s.length() - 3);
        } else if(s.length() > 4) {
            s = s.substring(0, s.length() - 4) + "," + s.substring(s.length() - 3, s.length());
        }
        return s;
    }

    public final int inventoryCount(int i) {
        int j = 0;
        for(int k = 0; k < inventoryCount; k++)
            if(inventoryItems[k] == i)
                if(Data.itemStackable[i] == 1)
                    j++;
                else
                    j += inventoryItemsCount[k];
        return j;
    }

    public byte[] load(String path, String title, int percentage) {
        int expectedTotal = 0;
        int total = 0;
        byte[] buffer = null;
        try {
            drawLoadingBarText(percentage, "Loading " + title + " - 0%");
            try(DataInputStream datainputstream = new DataInputStream(new FileInputStream(AppletUtils.CACHE + "/" + path))) {
                byte[] header = new byte[6];
                datainputstream.readFully(header, 0, 6);
                expectedTotal = ((header[0] & 0xFF) << 16) + ((header[1] & 0xFF) << 8) + (header[2] & 0xFF);
                /**
                 * expectedTotal and total are Smarts?
                 */
                total = ((header[3] & 0xFF) << 16) + ((header[4] & 0xFF) << 8) + (header[5] & 0xFF);
                drawLoadingBarText(percentage, "Loading " + title);
                int read = 0;
                buffer = new byte[total];
                while(read < total) {
                    int size = total - read;
                    if(size > 1000)
                        size = 1000;
                    datainputstream.readFully(buffer, read, size);
                    read += size;
                    // drawLoadingBarText(percentage, "Loading " + title + " - "
                    // + (5 + (read * 95) / total) + "%");
                    drawLoadingBarText(percentage, "Reading " + title);
                }
            }
        } catch(IOException _ex) {
            _ex.printStackTrace();
        }
        if(total != expectedTotal) {
            drawLoadingBarText(percentage, "Unpacking " + title);
            byte[] decompressed = new byte[expectedTotal];
            DataFileDecrypter.unpackData(decompressed, expectedTotal, buffer, total, 0);
            return decompressed;
        }
        return buffer;
    }

    private void loadConfigFilter() {
        byte config[] = load("config" + VERSION_CONFIG + ".jag", "Configuration", 10);
        if(config == null) {
            lastLoadedNull = true;
            return;
        }
        Data.loadData(config, member);
    }

    private void loadEntity() {
        byte[] entity = load("entity" + VERSION_ENTITY + ".jag", "people and monsters", 30);
        if(entity == null) {
            lastLoadedNull = true;
            return;
        }
        byte[] entityIndex = Utility.unpackConfigArchiveEntry("index.dat", 0, entity);
        byte entityMembers[] = null;
        byte entityIndexMembers[] = null;
        if(member) {
            entityMembers = load("entity" + VERSION_ENTITY + ".mem", "member graphics", 45);
            if(entityMembers == null) {
                lastLoadedNull = true;
                return;
            }
            entityIndexMembers = Utility.unpackConfigArchiveEntry("index.dat", 0, entityMembers);
        }
        animationZeroCount = 0;
        animationNumber = animationZeroCount;
        label0:
        for(int animationIndex = 0; animationIndex < Data.integerAnimationCount; animationIndex++) {
            String s = Data.animationName[animationIndex];
            for(int nextAnimationIndex = 0; nextAnimationIndex < animationIndex; nextAnimationIndex++) {
                if(!Data.animationName[nextAnimationIndex].equalsIgnoreCase(s))
                    continue;
                Data.animationNumber[animationIndex] = Data.animationNumber[nextAnimationIndex];
                continue label0;
            }
            byte[] animationData = Utility.unpackConfigArchiveEntry(s + ".dat", 0, entity);
            byte animationEntityIndexData[] = entityIndex;
            if(animationData == null && member) {
                animationData = Utility.unpackConfigArchiveEntry(s + ".dat", 0, entityMembers);
                animationEntityIndexData = entityIndexMembers;
            }
            if(animationData != null) {
                surface.loadAnimation(animationNumber, animationData, animationEntityIndexData, 15);
                if(Data.animationHasA[animationIndex] == 1) {
                    byte animationDataA[] = Utility.unpackConfigArchiveEntry(s + "a.dat", 0, entity);
                    byte animationEntityIndexDataA[] = entityIndex;
                    if(animationDataA == null && member) {
                        animationDataA = Utility.unpackConfigArchiveEntry(s + "a.dat", 0, entityMembers);
                        animationEntityIndexDataA = entityIndexMembers;
                    }
                    surface.loadAnimation(animationNumber + 15, animationDataA, animationEntityIndexDataA, 3);
                }
                if(Data.animationHasF[animationIndex] == 1) {
                    byte animationDataF[] = Utility.unpackConfigArchiveEntry(s + "f.dat", 0, entity);
                    byte animationEntityIndexDataF[] = entityIndex;
                    if(animationDataF == null && member) {
                        animationDataF = Utility.unpackConfigArchiveEntry(s + "f.dat", 0, entityMembers);
                        animationEntityIndexDataF = entityIndexMembers;
                    }
                    surface.loadAnimation(animationNumber + 18, animationDataF, animationEntityIndexDataF, 9);
                }
                if(Data.animationGenderModels[animationIndex] != 0) {
                    for(int l = animationNumber; l < animationNumber + 27; l++)
                        surface.method227(l);
                }
            }
            Data.animationNumber[animationIndex] = animationNumber;
            animationNumber += 27;
        }
    }

    private void loadFonts() {
        Graphics gfx = this.delegate.getContainerImpl().getGraphics();
        gfx.setColor(Color.black);
        gfx.fillRect(0, 0, this.delegate.getContainerImpl().getWidth(), this.delegate.getContainerImpl().getHeight());
        byte[] buff = load("jagex.jag", "SlackNet Library", 0);
        try {
            LOADING_IMAGE = ImageIO.read(new ByteArrayInputStream(Utility.unpackConfigArchiveEntry("logo.tga", 0, buff)));
        } catch(IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String[] font_names = { "h11p.jf", "h12b.jf", "h12p.jf", "h13b.jf", "h14b.jf", "h16b.jf", "h20b.jf", "h24b.jf" };
        Surface.font_count = font_names.length;
        for(String font_name : font_names)
            Surface.loadFont_(DataFileDecrypter.loadCachedData(font_name, buff));
        drawLoadingScreen();
    }

    private void loadMedia() {
        byte media[] = load("media" + VERSION_MEDIA + ".jag", "2d graphics", 20);
        if(media == null) {
            lastLoadedNull = true;
            return;
        }
        byte mediaIndex[] = Utility.unpackConfigArchiveEntry("index.dat", 0, media);
        surface.loadAnimation(SPRITE_MEDIA, Utility.unpackConfigArchiveEntry("inv1.dat", 0, media), mediaIndex, 1);
        surface.loadAnimation(SPRITE_MEDIA + 1, Utility.unpackConfigArchiveEntry("inv2.dat", 0, media), mediaIndex, 6);
        surface.loadAnimation(SPRITE_MEDIA + 9, Utility.unpackConfigArchiveEntry("bubble.dat", 0, media), mediaIndex, 1);
        surface.loadAnimation(SPRITE_MEDIA + 10, Utility.unpackConfigArchiveEntry("runescape.dat", 0, media), mediaIndex, 1);
        surface.loadAnimation(SPRITE_MEDIA + 11, Utility.unpackConfigArchiveEntry("splat.dat", 0, media), mediaIndex, 3);
        surface.loadAnimation(SPRITE_MEDIA + 14, Utility.unpackConfigArchiveEntry("icon.dat", 0, media), mediaIndex, 8);
        surface.loadAnimation(SPRITE_MEDIA + 22, Utility.unpackConfigArchiveEntry("hbar.dat", 0, media), mediaIndex, 1);
        surface.loadAnimation(SPRITE_MEDIA + 23, Utility.unpackConfigArchiveEntry("hbar2.dat", 0, media), mediaIndex, 1);
        surface.loadAnimation(SPRITE_MEDIA + 24, Utility.unpackConfigArchiveEntry("compass.dat", 0, media), mediaIndex, 1);
        surface.loadAnimation(SPRITE_MEDIA + 25, Utility.unpackConfigArchiveEntry("buttons.dat", 0, media), mediaIndex, 2);
        surface.loadAnimation(SPRITE_UTIL, Utility.unpackConfigArchiveEntry("scrollbar.dat", 0, media), mediaIndex, 2);
        surface.loadAnimation(SPRITE_UTIL + 2, Utility.unpackConfigArchiveEntry("corners.dat", 0, media), mediaIndex, 4);
        surface.loadAnimation(SPRITE_UTIL + 6, Utility.unpackConfigArchiveEntry("arrows.dat", 0, media), mediaIndex, 2);
        surface.loadAnimation(SPRITE_PROJECTILE, Utility.unpackConfigArchiveEntry("projectile.dat", 0, media), mediaIndex, Data.spellProjectileCount);
        /*
         * try {
         * Archive archive = new Archive(Utils.readFile(new File(
         * AppletUtils.CACHE + "/media_models.jag")));
         * surface.registerSprite(SPRITE_MEDIA, Sprite.unpack(new
         * Stream(archive.getFile(Archive.getHash("inv1.dat")))));
         * surface.registerSprite(SPRITE_MEDIA + 1, Sprite.unpack(new
         * Stream(archive.getFile(Archive.getHash("inv2.dat")))));
         * surface.registerSprite(SPRITE_MEDIA + 9, Sprite.unpack(new
         * Stream(archive.getFile(Archive.getHash("bubble.dat")))));
         * surface.registerSprite(SPRITE_MEDIA + 10, Sprite.unpack(new
         * Stream(archive.getFile(Archive.getHash("runescape.dat")))));
         * surface.registerSprite(SPRITE_MEDIA + 11, Sprite.unpack(new
         * Stream(archive.getFile(Archive.getHash("splat.dat")))));
         * surface.registerSprite(SPRITE_MEDIA + 14, Sprite.unpack(new
         * Stream(archive.getFile(Archive.getHash("icon.dat")))));
         * surface.registerSprite(SPRITE_MEDIA + 22, Sprite.unpack(new
         * Stream(archive.getFile(Archive.getHash("hbar.dat")))));
         * surface.registerSprite(SPRITE_MEDIA + 23, Sprite.unpack(new
         * Stream(archive.getFile(Archive.getHash("hbar2.dat")))));
         * surface.registerSprite(SPRITE_MEDIA + 24, Sprite.unpack(new
         * Stream(archive.getFile(Archive.getHash("compass.dat")))));
         * surface.registerSprite(SPRITE_MEDIA + 25, Sprite.unpack(new
         * Stream(archive.getFile(Archive.getHash("buttons.dat")))));
         * surface.registerSprite(SPRITE_UTIL, Sprite.unpack(new
         * Stream(archive.getFile(Archive.getHash("scrollbar.dat")))));
         * surface.registerSprite(SPRITE_UTIL + 2, Sprite.unpack(new
         * Stream(archive.getFile(Archive.getHash("corners.dat")))));
         * surface.registerSprite(SPRITE_UTIL + 6, Sprite.unpack(new
         * Stream(archive.getFile(Archive.getHash("arrows.dat")))));
         * surface.registerSprite(SPRITE_PROJECTILE, Sprite.unpack(new
         * Stream(archive.getFile(Archive.getHash("projectile.dat")))));
         * }
         * catch(IOException e) {
         * e.printStackTrace();
         * }
         */
/*		try {
			Archive archive = new Archive(Utils.readFile(new File(
					AppletUtils.CACHE + "/item_models.jag")));
			for(int index = 0; index < archive.getTotalFiles(); index++) {
				surface.registerSprite(SPRITE_ITEM + index, Sprite
						.unpack(new Stream(archive.getFile(Archive
								.getHash(index + ".dat")))));
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}*/
        int i = Data.itemInventoryPictureCount;
        for(int j = 1; i > 0; j++) {
            int k = i;
            i -= 30;
            if(k > 30)
                k = 30;
            surface.loadAnimation(SPRITE_ITEM + (j - 1) * 30, Utility.unpackConfigArchiveEntry("objects" + j + ".dat", 0, media), mediaIndex, k);
        }
        surface.method227(SPRITE_MEDIA);
        surface.method227(SPRITE_MEDIA + 9);
        for(int l = 11; l <= 26; l++)
            surface.method227(SPRITE_MEDIA + l);
        for(int i1 = 0; i1 < Data.spellProjectileCount; i1++)
            surface.method227(SPRITE_PROJECTILE + i1);
    }

    private void loadModels() {
        Data.convertToModelInteger("torcha2");
        Data.convertToModelInteger("torcha3");
        Data.convertToModelInteger("torcha4");
        Data.convertToModelInteger("skulltorcha2");
        Data.convertToModelInteger("skulltorcha3");
        Data.convertToModelInteger("skulltorcha4");
        Data.convertToModelInteger("firea2");
        Data.convertToModelInteger("firea3");
        Data.convertToModelInteger("fireplacea2");
        Data.convertToModelInteger("fireplacea3");
        Data.convertToModelInteger("firespell2");
        Data.convertToModelInteger("firespell3");
        Data.convertToModelInteger("lightning2");
        Data.convertToModelInteger("lightning3");
        Data.convertToModelInteger("clawspell2");
        Data.convertToModelInteger("clawspell3");
        Data.convertToModelInteger("clawspell4");
        Data.convertToModelInteger("clawspell5");
        Data.convertToModelInteger("spellcharge2");
        Data.convertToModelInteger("spellcharge3");
        try {
            Archive models = new Archive(Utils.readFile(new File(AppletUtils.CACHE + "/models" + VERSION_MODELS + ".jag")));
            for(int j = 0; j < Data.modelCount; j++) {
                int hash = Archive.getHash(Data.modelNames[j] + ".ob3");
                if(models.getFile(hash) != null)
                    gameModels[j] = new GameModel(models.getFile(hash), true);
                else
                    gameModels[j] = new GameModel(1, 1);
                if(Data.modelNames[j].equals("giantcrystal"))
                    gameModels[j].transparent = true;
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        /*
         * byte models[] = load("models" + Version.VERSION_MODELS + ".jag",
         * "3d models", 60);
         * if(models == null) {
         * lastLoadedNull = true;
         * return;
         * }
         * for(int j = 0; j < Data.modelCount; j++) {
         * int k = Utility.getDataOffset(Data.modelNames[j] + ".ob3", models);
         * if(k != 0)
         * gameModels[j] = new GameModel(models, k, true);
         * else
         * gameModels[j] = new GameModel(1, 1);
         * if(Data.modelNames[j].equals("giantcrystal"))
         * gameModels[j].transparent = true;
         * }
         */
    }

    public final boolean loadSection(int i, int j) {
        notInWilderness = false;
        i += wildX;
        j += wildY;
        if(lastWildYSubtract == currentPlane && i > anInt789 && i < anInt791 && j > anInt790 && j < anInt792) {
            world.playerIsAlive = true;
            return false;
        }
        surface.drawStringCentered("Loading... Please wait", 256, 192, 1, 0xffffff);
        drawChatMessageTabs();
        surface.draw(aGraphics936, 0, 0);
        int k = areaX;
        int l = areaY;
        int i1 = (i + 24) / 48;
        int j1 = (j + 24) / 48;
        // System.out.println("X:" + i1 + ",Y:" + j1);
        lastWildYSubtract = currentPlane;
        areaX = i1 * 48 - 48;
        areaY = j1 * 48 - 48;
        anInt789 = i1 * 48 - 32;
        anInt790 = j1 * 48 - 32;
        anInt791 = i1 * 48 + 32;
        anInt792 = j1 * 48 + 32;
        areaX -= wildX;
        areaY -= wildY;
        world.set(areaX, areaY);
        world.populateSection(i, j, lastWildYSubtract);
        int k1 = areaX - k;
        int l1 = areaY - l;
        for(int i2 = 0; i2 < objectCount; i2++) {
            objectX[i2] -= k1;
            objectY[i2] -= l1;
            int j2 = objectX[i2];
            int l2 = objectY[i2];
            int k3 = objectType[i2];
            int m4 = objectID[i2];
            GameModel model = objectModelArray[i2];
            try {
                int l4 = objectID[i2];
                int k5;
                int i6;
                if(l4 == 0 || l4 == 4) {
                    k5 = Data.objectWidth[k3];
                    i6 = Data.objectHeight[k3];
                } else {
                    i6 = Data.objectWidth[k3];
                    k5 = Data.objectHeight[k3];
                }
                int j6 = ((j2 + j2 + k5) * magicLoc) / 2;
                int k6 = ((l2 + l2 + i6) * magicLoc) / 2;
                if(j2 >= 0 && l2 >= 0 && j2 < 96 && l2 < 96) {
                    scene.addModel(model);
                    model.place(j6, -world.getElevation(j6, k6), k6);
                    world.addObject(j2, l2, k3, m4);
                    if(k3 == 74)
                        model.translate(0, -480, 0);
                }
            } catch(RuntimeException runtimeexception) {
                System.out.println("Loc Error: " + runtimeexception.getMessage());
                System.out.println("i:" + i2 + " obj:" + model);
                runtimeexception.printStackTrace();
            }
        }
        for(int k2 = 0; k2 < wallObjectCount; k2++) {
            wallObjectX[k2] -= k1;
            wallObjectY[k2] -= l1;
            int i3 = wallObjectX[k2];
            int l3 = wallObjectY[k2];
            int j4 = wallObjectType[k2];
            int i5 = wallObjectDirection[k2];
            try {
                world.setModelAdjacency(i3, l3, i5, j4);
                GameModel model_1 = addModel(i3, l3, i5, j4, k2);
                wallObjectModel[k2] = model_1;
            } catch(RuntimeException runtimeexception1) {
                System.out.println("Bound Error: " + runtimeexception1.getMessage());
                runtimeexception1.printStackTrace();
            }
        }
        for(int j3 = 0; j3 < groundItemCount; j3++) {
            groundItemX[j3] -= k1;
            groundItemY[j3] -= l1;
        }
        for(int i4 = 0; i4 < playerCount; i4++) {
            Actor mob = playerArray[i4];
            mob.currentX -= k1 * magicLoc;
            mob.currentY -= l1 * magicLoc;
            for(int j5 = 0; j5 <= mob.waypointCurrent; j5++) {
                mob.waypointsX[j5] -= k1 * magicLoc;
                mob.waypointsY[j5] -= l1 * magicLoc;
            }
        }
        for(int k4 = 0; k4 < npcCount; k4++) {
            Actor mob_1 = npcs[k4];
            mob_1.currentX -= k1 * magicLoc;
            mob_1.currentY -= l1 * magicLoc;
            for(int l5 = 0; l5 <= mob_1.waypointCurrent; l5++) {
                mob_1.waypointsX[l5] -= k1 * magicLoc;
                mob_1.waypointsY[l5] -= l1 * magicLoc;
            }
        }
        world.playerIsAlive = true;
        return true;
    }

    private void loadTextures() {
        byte textures[] = load("textures" + VERSION_TEXTURES + ".jag", "Textures", 50);
        if(textures == null) {
            lastLoadedNull = true;
            return;
        }
        byte abyte1[] = Utility.unpackConfigArchiveEntry("index.dat", 0, textures);
        scene.method297(Data.textureFileCount, 7, 11);
        for(int i = 0; i < Data.textureFileCount; i++) {
            String s = Data.dataFileNames[i];
            byte abyte2[] = Utility.unpackConfigArchiveEntry(s + ".dat", 0, textures);
            surface.loadAnimation(SPRITE_TEXTURE, abyte2, abyte1, 1);
            surface.drawBox(0, 0, 128, 128, 0xff00ff);
            surface.drawSprite(0, 0, SPRITE_TEXTURE);
            int j = surface.imageFullWidth[SPRITE_TEXTURE];
            String s1 = Data.animationFileName[i];
            if(s1 != null && s1.length() > 0) {
                byte abyte3[] = Utility.unpackConfigArchiveEntry(s1 + ".dat", 0, textures);
                surface.loadAnimation(SPRITE_TEXTURE, abyte3, abyte1, 1);
                surface.drawSprite(0, 0, SPRITE_TEXTURE);
            }
            surface.method229(SPRITE_TEXTURE_WORLD + i, 0, 0, j, j);
            int k = j * j;
            for(int l = 0; l < k; l++)
                if(surface.surfacePixels[SPRITE_TEXTURE_WORLD + i][l] == 65280)
                    ((Surface) surface).surfacePixels[SPRITE_TEXTURE_WORLD + i][l] = 0xff00ff;
            surface.drawWorld(SPRITE_TEXTURE_WORLD + i);
            scene.method298(i, surface.aByteArrayArray322[SPRITE_TEXTURE_WORLD + i], surface.anIntArrayArray323[SPRITE_TEXTURE_WORLD + i], j / 64 - 1);
        }
    }

    @Override
    protected final void loginScreenPrint(String s, String s1) {
        if(loginScreenNumber == 1)
            menuNewUser.updateText(anInt900, s + " " + s1);
        if(loginScreenNumber == 2)
            menuLogin.updateText(loginStatusText, s + " " + s1);
        drawLoginScreen();
        resetCurrentTimeArray();
    }

    private void logout() {
        if(loggedIn == 0)
            return;
        if(lastWalkTimeout > 450) {
            displayMessage("@cya@You can't logout during combat!", 3, null);
            return;
        }
        if(lastWalkTimeout > 0) {
            displayMessage("@cya@You can't logout for 10 seconds after combat", 3, null);
        } else {
            super.streamClass.addNewFrame(145);
            super.streamClass.formatCurrentFrame();
            logoutTimeout = 1000;
        }
    }

    @Override
    protected final void logoutAndStop() {
        sendLogoutPacket();
        garbageCollect();
        if(soundInputStream != null)
            soundInputStream = null;
    }

    @Override
    protected final void lostConnection() {
        systemUpdate = 0;
        if(logoutTimeout != 0) {
            resetIntVars();
        } else {
            super.lostConnection();
        }
    }

    private void makeCharacterDesignMenu() {
        characterDesignMenu = new Menu(surface, 100);
        characterDesignMenu.drawText(gameWidth / 2, 10, "Please design Your Character", 4, true);
        int i = (gameWidth - 215) / 2;
        int j = gameHeight / 2 - 141;
        i += 116;
        j -= 10;
        characterDesignMenu.drawText(i - 55, j + 110, "Front", 3, true);
        characterDesignMenu.drawText(i, j + 110, "Side", 3, true);
        characterDesignMenu.drawText(i + 55, j + 110, "Back", 3, true);
        byte byte0 = 54;
        j += 145;
        characterDesignMenu.method157(i - byte0, j, 53, 41);
        characterDesignMenu.drawText(i - byte0, j - 8, "Head", 1, true);
        characterDesignMenu.drawText(i - byte0, j + 8, "Type", 1, true);
        characterDesignMenu.method158(i - byte0 - 40, j, Menu.anInt221 + 7);
        characterDesignHeadButton1 = characterDesignMenu.makeButton(i - byte0 - 40, j, 20, 20);
        characterDesignMenu.method158((i - byte0) + 40, j, Menu.anInt221 + 6);
        characterDesignHeadButton2 = characterDesignMenu.makeButton((i - byte0) + 40, j, 20, 20);
        characterDesignMenu.method157(i + byte0, j, 53, 41);
        characterDesignMenu.drawText(i + byte0, j - 8, "Hair", 1, true);
        characterDesignMenu.drawText(i + byte0, j + 8, "Color", 1, true);
        characterDesignMenu.method158((i + byte0) - 40, j, Menu.anInt221 + 7);
        characterDesignHairColorButton1 = characterDesignMenu.makeButton((i + byte0) - 40, j, 20, 20);
        characterDesignMenu.method158(i + byte0 + 40, j, Menu.anInt221 + 6);
        characterDesignHairColorButton2 = characterDesignMenu.makeButton(i + byte0 + 40, j, 20, 20);
        j += 50;
        characterDesignMenu.method157(i - byte0, j, 53, 41);
        characterDesignMenu.drawText(i - byte0, j, "Gender", 1, true);
        characterDesignMenu.method158(i - byte0 - 40, j, Menu.anInt221 + 7);
        characterDesignGenderButton1 = characterDesignMenu.makeButton(i - byte0 - 40, j, 20, 20);
        characterDesignMenu.method158((i - byte0) + 40, j, Menu.anInt221 + 6);
        characterDesignGenderButton2 = characterDesignMenu.makeButton((i - byte0) + 40, j, 20, 20);
        characterDesignMenu.method157(i + byte0, j, 53, 41);
        characterDesignMenu.drawText(i + byte0, j - 8, "Top", 1, true);
        characterDesignMenu.drawText(i + byte0, j + 8, "Color", 1, true);
        characterDesignMenu.method158((i + byte0) - 40, j, Menu.anInt221 + 7);
        characterDesignTopColorButton1 = characterDesignMenu.makeButton((i + byte0) - 40, j, 20, 20);
        characterDesignMenu.method158(i + byte0 + 40, j, Menu.anInt221 + 6);
        characterDesignTopColorButton2 = characterDesignMenu.makeButton(i + byte0 + 40, j, 20, 20);
        j += 50;
        characterDesignMenu.method157(i - byte0, j, 53, 41);
        characterDesignMenu.drawText(i - byte0, j - 8, "Skin", 1, true);
        characterDesignMenu.drawText(i - byte0, j + 8, "Color", 1, true);
        characterDesignMenu.method158(i - byte0 - 40, j, Menu.anInt221 + 7);
        characterDesignSkinColorButton1 = characterDesignMenu.makeButton(i - byte0 - 40, j, 20, 20);
        characterDesignMenu.method158((i - byte0) + 40, j, Menu.anInt221 + 6);
        characterDesignSkinColorButton2 = characterDesignMenu.makeButton((i - byte0) + 40, j, 20, 20);
        characterDesignMenu.method157(i + byte0, j, 53, 41);
        characterDesignMenu.drawText(i + byte0, j - 8, "Bottom", 1, true);
        characterDesignMenu.drawText(i + byte0, j + 8, "Color", 1, true);
        characterDesignMenu.method158((i + byte0) - 40, j, Menu.anInt221 + 7);
        characterDesignBottomColorButton1 = characterDesignMenu.makeButton((i + byte0) - 40, j, 20, 20);
        characterDesignMenu.method158(i + byte0 + 40, j, Menu.anInt221 + 6);
        characterDesignBottomColorButton2 = characterDesignMenu.makeButton(i + byte0 + 40, j, 20, 20);
        j += 82;
        j -= 35;
        characterDesignMenu.newBox(i, j, 200, 30);
        characterDesignMenu.drawText(i, j, "Accept", 4, false);
        characterDesignAcceptButton = characterDesignMenu.makeButton(i, j, 200, 30);
    }

    public final void makeLoginMenus() {
        menuWelcome = new Menu(surface, 50);
        int i = 48;
        menuWelcome.drawText(windowWidth / 2, windowHeight / 2 + 25 + i, "Welcome to RuneScape Classic", 4, true);
        menuWelcome.drawText(windowWidth / 2, windowHeight / 2 + 40 + i, "You need an account to use this server", 4, true);
        menuWelcome.newBox(windowWidth / 2, windowHeight / 2 + 75 + i, 200, 35);
        menuWelcome.drawText(windowWidth / 2, windowHeight / 2 + 75 + i, "Click here to login", 5, false);
        loginButtonExistingUser = menuWelcome.makeButton(windowWidth / 2, windowHeight / 2 + 73 + i, 200, 35);
        menuNewUser = new Menu(surface, 50);
        i = windowHeight / 2 + 63;
        menuNewUser.drawText(windowWidth / 2, i + 8, "To create an account please go back to the", 4, true);
        i += 20;
        menuNewUser.drawText(windowWidth / 2, i + 8, "rscemulation.net front page, and choose 'register'", 4, true);
        i += 30;
        menuNewUser.newBox(windowWidth / 2, i + 17, 150, 34);
        menuNewUser.drawText(windowWidth / 2, i + 17, "Ok", 5, false);
        newUserOkButton = menuNewUser.makeButton(windowWidth / 2, i + 17, 150, 34);
        menuLogin = new Menu(surface, 50);
        i = windowHeight / 2 + 63;
        loginStatusText = menuLogin.drawText(windowWidth / 2, i - 10, "Please enter your username and password", 4, true);
        i += 28;
        menuLogin.newBox(windowWidth / 2 - 116, i, 200, 40);
        menuLogin.drawText(windowWidth / 2 - 116, i - 10, "Username:", 4, false);
        loginUsernameTextBox = menuLogin.makeTextBox(windowWidth / 2 - 116, i + 10, 200, 40, 4, 12, false, false);
        i += 47;
        menuLogin.newBox(windowWidth / 2 - 66, i, 200, 40);
        menuLogin.drawText(windowWidth / 2 - 66, i - 10, "Password:", 4, false);
        loginPasswordTextBox = menuLogin.makeTextBox(windowWidth / 2 - 66, i + 10, 200, 40, 4, 20, true, false);
        i -= 55;
        menuLogin.newBox(windowWidth / 2 + 154, i, 120, 25);
        menuLogin.drawText(windowWidth / 2 + 154, i, "Ok", 4, false);
        loginOkButton = menuLogin.makeButton(windowWidth / 2 + 154, i, 120, 25);
        i += 30;
        menuLogin.newBox(windowWidth / 2 + 154, i, 120, 25);
        menuLogin.drawText(windowWidth / 2 + 154, i, "Cancel", 4, false);
        loginCancelButton = menuLogin.makeButton(windowWidth / 2 + 154, i, 120, 25);
        i += 25;
        menuLogin.setFocus(loginUsernameTextBox);
    }

    public final Actor makePlayer(int mobArrayIndex, int x, int y, int sprite, int group) {
        if(mobArray[mobArrayIndex] == null) {
            mobArray[mobArrayIndex] = new Actor();
            mobArray[mobArrayIndex].serverIndex = mobArrayIndex;
            mobArray[mobArrayIndex].mobIntUnknown = 0;
        }
        Actor mob = mobArray[mobArrayIndex];
        boolean flag = false;
        for(int i1 = 0; i1 < knownPlayerCount; i1++) {
            if(knownPlayers[i1].serverIndex != mobArrayIndex)
                continue;
            flag = true;
            break;
        }
        if(flag) {
            mob.nextSprite = sprite;
            int j1 = mob.waypointCurrent;
            if(x != mob.waypointsX[j1] || y != mob.waypointsY[j1]) {
                mob.waypointCurrent = j1 = (j1 + 1) % 10;
                mob.waypointsX[j1] = x;
                mob.waypointsY[j1] = y;
            }
        } else {
            mob.serverIndex = mobArrayIndex;
            mob.waypointEndSprite = 0;
            mob.waypointCurrent = 0;
            mob.waypointsX[0] = mob.currentX = x;
            mob.waypointsY[0] = mob.currentY = y;
            mob.nextSprite = mob.currentAnimation = sprite;
            mob.stepCount = 0;
        }
        mob.group = group;
        playerArray[playerCount++] = mob;
        return mob;
    }

    public void makeTest() {
        if(spellMenu != null) {
            int l = surface.menuMaxWidth - 199;
            spellMenu.resize(spellMenuHandle, l, 60, 196, 90);
            friendsMenu.resize(friendsMenuHandle, l, 76, 196, 126);
        }
    }

    public void menuClear() {
        for(int jx = 0; jx < menuLength; jx++) {
            menuText1[jx] = null;
            menuText2[jx] = null;
            menuActionVariable[jx] = -1;
            menuActionVariable2[jx] = -1;
            menuID[jx] = -1;
        }
    }

    public final void menuClick(int index) {
        int actionX = menuActionX[index];
        int actionY = menuActionY[index];
        int actionType = menuActionType[index];
        int actionVariable = menuActionVariable[index];
        int actionVariable2 = menuActionVariable2[index];
        int currentMenuID = menuID[index];
        if(currentMenuID == 200) {
            walkToGroundItem(regionX, regionY, actionX, actionY, true);
            super.streamClass.addNewFrame(104);
            super.streamClass.addShort(actionVariable);
            super.streamClass.addShort(actionX + areaX);
            super.streamClass.addShort(actionY + areaY);
            super.streamClass.addShort(actionType);
            super.streamClass.formatCurrentFrame();
            selectedSpell = -1;
        }
        if(currentMenuID == 210) {
            walkToGroundItem(regionX, regionY, actionX, actionY, true);
            super.streamClass.addNewFrame(34);
            super.streamClass.addShort(actionX + areaX);
            super.streamClass.addShort(actionY + areaY);
            super.streamClass.addShort(actionType);
            super.streamClass.addShort(actionVariable);
            super.streamClass.formatCurrentFrame();
            selectedItem = -1;
        }
        if(currentMenuID == 220) {
            walkToGroundItem(regionX, regionY, actionX, actionY, true);
            super.streamClass.addNewFrame(182);
            super.streamClass.addShort(actionX + areaX);
            super.streamClass.addShort(actionY + areaY);
            super.streamClass.addShort(actionType);
            super.streamClass.addShort(actionVariable);
            super.streamClass.formatCurrentFrame();
        }
        if(currentMenuID == 300) {
            walkToBoundary(actionX, actionY, actionType);
            super.streamClass.addNewFrame(67);
            super.streamClass.addShort(actionVariable);
            super.streamClass.addShort(actionX + areaX);
            super.streamClass.addShort(actionY + areaY);
            super.streamClass.addByte(actionType);
            super.streamClass.formatCurrentFrame();
            selectedSpell = -1;
        }
        if(currentMenuID == 310) {
            walkToBoundary(actionX, actionY, actionType);
            super.streamClass.addNewFrame(241);
            super.streamClass.addShort(actionX + areaX);
            super.streamClass.addShort(actionY + areaY);
            super.streamClass.addByte(actionType);
            super.streamClass.addShort(actionVariable);
            super.streamClass.formatCurrentFrame();
            selectedItem = -1;
        }
        if(currentMenuID == 320) {
            walkToBoundary(actionX, actionY, actionType);
            super.streamClass.addNewFrame(126);
            super.streamClass.addShort(actionX + areaX);
            super.streamClass.addShort(actionY + areaY);
            super.streamClass.addByte(actionType);
            super.streamClass.formatCurrentFrame();
        }
        if(currentMenuID == 2300) {
            walkToBoundary(actionX, actionY, actionType);
            super.streamClass.addNewFrame(235);
            super.streamClass.addShort(actionX + areaX);
            super.streamClass.addShort(actionY + areaY);
            super.streamClass.addByte(actionType);
            super.streamClass.formatCurrentFrame();
        }
        if(currentMenuID == 400) {
            walkToObject(actionX, actionY, actionType, actionVariable);
            super.streamClass.addNewFrame(17);
            super.streamClass.addShort(actionVariable2);
            super.streamClass.addShort(actionX + areaX);
            super.streamClass.addShort(actionY + areaY);
            super.streamClass.formatCurrentFrame();
            selectedSpell = -1;
        }
        if(currentMenuID == 410) {
            walkToObject(actionX, actionY, actionType, actionVariable);
            super.streamClass.addNewFrame(94);
            super.streamClass.addShort(actionX + areaX);
            super.streamClass.addShort(actionY + areaY);
            super.streamClass.addShort(actionVariable2);
            super.streamClass.formatCurrentFrame();
            selectedItem = -1;
        }
        if(currentMenuID == 420) {
            walkToObject(actionX, actionY, actionType, actionVariable);
            super.streamClass.addNewFrame(13);
            super.streamClass.addShort(actionX + areaX);
            super.streamClass.addShort(actionY + areaY);
            super.streamClass.formatCurrentFrame();
        }
        if(currentMenuID == 2400) {
            walkToObject(actionX, actionY, actionType, actionVariable);
            super.streamClass.addNewFrame(75);
            super.streamClass.addShort(actionX + areaX);
            super.streamClass.addShort(actionY + areaY);
            super.streamClass.formatCurrentFrame();
        }
        if(currentMenuID == 3403) {
            sendCommand("dobj " + (actionX + areaX) + " " + (actionY + areaY));
        }
        if(currentMenuID == 600) {
            super.streamClass.addNewFrame(49);
            super.streamClass.addShort(actionVariable);
            super.streamClass.addShort(actionType);
            super.streamClass.formatCurrentFrame();
            selectedSpell = -1;
        }
        if(currentMenuID == 610) {
            super.streamClass.addNewFrame(27);
            super.streamClass.addShort(actionType);
            super.streamClass.addShort(actionVariable);
            super.streamClass.formatCurrentFrame();
            selectedItem = -1;
        }
        if(currentMenuID == 620) {
            super.streamClass.addNewFrame(158);
            super.streamClass.addShort(actionType);
            super.streamClass.formatCurrentFrame();
        }
        if(currentMenuID == 630) {
            super.streamClass.addNewFrame(19);
            super.streamClass.addShort(actionType);
            super.streamClass.formatCurrentFrame();
        }
        if(currentMenuID == 640) {
            super.streamClass.addNewFrame(219);
            super.streamClass.addShort(actionType);
            super.streamClass.formatCurrentFrame();
        }
        if(currentMenuID == 650) {
            selectedItem = actionType;
            mouseOverMenu = 0;
            selectedItemName = Data.itemName[this.inventoryItems[actionType]];
        }
        if(currentMenuID == 660) {
            super.streamClass.addNewFrame(38);
            super.streamClass.addShort(actionType);
            super.streamClass.formatCurrentFrame();
            selectedItem = -1;
            mouseOverMenu = 0;
        }
        if(currentMenuID == 700) {
            int l1 = (actionX - 64) / magicLoc;
            int l3 = (actionY - 64) / magicLoc;
            method112(regionX, regionY, l1, l3, true);
            super.streamClass.addNewFrame(89);
            super.streamClass.addShort(actionVariable);
            super.streamClass.addShort(actionType);
            super.streamClass.formatCurrentFrame();
            selectedSpell = -1;
        }
        if(currentMenuID == 710) {
            int i2 = (actionX - 64) / magicLoc;
            int i4 = (actionY - 64) / magicLoc;
            method112(regionX, regionY, i2, i4, true);
            super.streamClass.addNewFrame(142);
            super.streamClass.addShort(actionType);
            super.streamClass.addShort(actionVariable);
            super.streamClass.formatCurrentFrame();
            selectedItem = -1;
        }
        if(currentMenuID == 721) {
            super.streamClass.addNewFrame(190);
            super.streamClass.formatCurrentFrame();
        }
        if(currentMenuID == 722) {
            showPetInventory = true;
        }
        if(currentMenuID == 720) {
            int j2 = (actionX - 64) / magicLoc;
            int j4 = (actionY - 64) / magicLoc;
            method112(regionX, regionY, j2, j4, true);
            super.streamClass.addNewFrame(189);
            super.streamClass.addShort(actionType);
            super.streamClass.formatCurrentFrame();
        }
        if(currentMenuID == 723) {
            sendCommand("ninfo " + actionType);
        }
        if(currentMenuID == 725) {
            int k2 = (actionX - 64) / magicLoc;
            int k4 = (actionY - 64) / magicLoc;
            method112(regionX, regionY, k2, k4, true);
            super.streamClass.addNewFrame(74);
            super.streamClass.addShort(actionType);
            super.streamClass.formatCurrentFrame();
        }
        if(currentMenuID == 715 || currentMenuID == 2715) {
            int l2 = (actionX - 64) / magicLoc;
            int l4 = (actionY - 64) / magicLoc;
            method112(regionX, regionY, l2, l4, true);
            super.streamClass.addNewFrame(92);
            super.streamClass.addShort(actionType);
            super.streamClass.formatCurrentFrame();
        }
        if(currentMenuID == 2716) {
            super.streamClass.addNewFrame(93);
            super.streamClass.addShort(actionType);
            super.streamClass.formatCurrentFrame();
        }
        if(currentMenuID == 800) {
            int i3 = (actionX - 64) / magicLoc;
            int i5 = (actionY - 64) / magicLoc;
            method112(regionX, regionY, i3, i5, true);
            super.streamClass.addNewFrame(223);
            super.streamClass.addShort(actionVariable);
            super.streamClass.addShort(actionType);
            super.streamClass.formatCurrentFrame();
            selectedSpell = -1;
        }
        if(currentMenuID == 810) {
            int j3 = (actionX - 64) / magicLoc;
            int j5 = (actionY - 64) / magicLoc;
            method112(regionX, regionY, j3, j5, true);
            super.streamClass.addNewFrame(231);
            super.streamClass.addShort(actionType);
            super.streamClass.addShort(actionVariable);
            super.streamClass.formatCurrentFrame();
            selectedItem = -1;
        }
        if(currentMenuID == 805 || currentMenuID == 2805) {
            int k3 = (actionX - 64) / magicLoc;
            int k5 = (actionY - 64) / magicLoc;
            method112(regionX, regionY, k3, k5, true);
            super.streamClass.addNewFrame(91);
            super.streamClass.addShort(actionType);
            super.streamClass.formatCurrentFrame();
        }
        if(currentMenuID == 2806) {
            super.streamClass.addNewFrame(98);
            super.streamClass.addShort(actionType);
            super.streamClass.formatCurrentFrame();
        }
        if(currentMenuID == 2809) {
            super.streamClass.addNewFrame(69);
            super.streamClass.addShort(actionType);
            super.streamClass.formatCurrentFrame();
        }
        if(currentMenuID == 2810) {
            super.streamClass.addNewFrame(251);
            super.streamClass.addShort(actionType);
            super.streamClass.formatCurrentFrame();
        }
        if(currentMenuID == 2807) {
            super.streamClass.addNewFrame(56);
            super.streamClass.addShort(actionType);
            super.streamClass.formatCurrentFrame();
        }
        if(currentMenuID == 2820) {
            super.streamClass.addNewFrame(76);
            super.streamClass.addShort(actionType);
            super.streamClass.formatCurrentFrame();
        }
        if(currentMenuID == 2830) {
            actionPictureType = -24;
            actionPictureX = super.mouseX; // guessing the little red/yellow x
            // that appears when you click
            actionPictureY = super.mouseY;
            sendCommand("kick " + mobArray[actionType].serverIndex);
        }
        if(currentMenuID == 2840) {
            actionPictureType = -24;
            actionPictureX = super.mouseX; // guessing the little red/yellow x
            // that appears when you click
            actionPictureY = super.mouseY;
            sendCommand("ban " + mobArray[actionType].name.replace(" ", "_") + " 0");
        }
        if(currentMenuID == 900) {
            method112(regionX, regionY, actionX, actionY, true);
            super.streamClass.addNewFrame(232);
            super.streamClass.addShort(actionType);
            super.streamClass.addShort(actionX + areaX);
            super.streamClass.addShort(actionY + areaY);
            super.streamClass.formatCurrentFrame();
            selectedSpell = -1;
        }
        if(currentMenuID == 920) {
            method112(regionX, regionY, actionX, actionY, false);
            if(actionPictureType == -24)
                actionPictureType = 24;
        }
        if(currentMenuID == 1000) {
            super.streamClass.addNewFrame(206);
            super.streamClass.addShort(actionType);
            super.streamClass.formatCurrentFrame();
            selectedSpell = -1;
        }
        if(currentMenuID == 4000) {
            selectedItem = -1;
            selectedSpell = -1;
        }
        if(currentMenuID == 878) {
            super.streamClass.addNewFrame(25);
            super.streamClass.addShort(actionVariable);
            super.streamClass.addInt(inventoryCount(actionVariable));
            super.streamClass.formatCurrentFrame();
        }
        if(currentMenuID == 784) {
            super.streamClass.addNewFrame(24);
            super.streamClass.addShort(actionVariable);
            super.streamClass.addInt(actionVariable2);
            super.streamClass.formatCurrentFrame();
        }
        if(currentMenuID == 888) {
            super.inputText = "";
            super.enteredText = "";
            inputBoxType = 5;
            inputID = actionVariable;
        }
        if(currentMenuID == 790) {
            super.inputText = "";
            super.enteredText = "";
            deposit = true;
            inputBoxType = 4;
            inputID = actionVariable;
        }
        if(currentMenuID == 787) {
            super.inputText = "";
            super.enteredText = "";
            deposit = false;
            inputBoxType = 4;
            inputID = actionVariable;
        }
        if(currentMenuID == 783)
            removeTradeItems(actionVariable, actionVariable2, actionType);
        if(currentMenuID == 782)
            addTradeItems(actionVariable, actionVariable2, actionType, false);
        if(currentMenuID == 786)
            doBankFunction(actionVariable, actionVariable2, false);
        if(currentMenuID == 788)
            doBankFunction(actionVariable, actionVariable2, true);
        if(currentMenuID == 881) {
            super.inputText = "";
            super.enteredText = "";
            inputBoxType = 7;
            inputID = actionVariable;
        }
        if(currentMenuID == 882) {
            if(duelMyItemCount == 8)
                return;
            addDuelItems(actionVariable, actionVariable2, actionType, false);
        }
        if(currentMenuID == 883)
            removeDuelItems(actionVariable, actionVariable2, actionType);
        if(currentMenuID == 889) {
            super.inputText = "";
            super.enteredText = "";
            inputBoxType = 9;
            inputID = actionVariable;
        }
        if(currentMenuID == 890) {
            super.inputText = "";
            super.enteredText = "";
            inputBoxType = 8;
            inputID = actionVariable;
        }
        if(currentMenuID == 789) {
            super.inputText = "";
            super.enteredText = "";
            inputBoxType = 6;
            inputID = actionVariable;
        }
        if(currentMenuID == 3200 || currentMenuID == 3600)
            displayMessage(Data.itemDescription[actionType], 3, ourPlayer);
        if(currentMenuID == 3300)
            displayMessage(Data.doorDescription[actionType], 3, ourPlayer);
        if(currentMenuID == 3400)
            displayMessage(Data.objectDescription[actionType], 3, ourPlayer);
        if(currentMenuID == 3700)
            displayMessage(Data.npcDescription[actionType], 3, ourPlayer);
    }

    private void method112(int i, int j, int k, int l, boolean flag) {
        sendWalkCommand(i, j, k, l, k, l, false, flag);
    }

    private boolean method117(int i) {
        for(int j = 0; j < inventoryCount; j++)
            if(inventoryItems[j] == i && wearing[j] == 1)
                return true;
        return false;
    }

    private void method119() {
        for(int i = 0; i < mobMessageCount; i++) {
            int j = surface.stringHeight(1);
            int l = mobMessagesX[i];
            int k1 = mobMessagesY[i];
            int j2 = mobMessagesWidth[i];
            int i3 = mobMessagesHeight[i];
            boolean flag = true;
            while(flag) {
                flag = false;
                for(int i4 = 0; i4 < i; i4++)
                    if(k1 + i3 > mobMessagesY[i4] - j && k1 - j < mobMessagesY[i4] + mobMessagesHeight[i4] && l - j2 < mobMessagesX[i4] + mobMessagesWidth[i4] && l + j2 > mobMessagesX[i4] - mobMessagesWidth[i4] && mobMessagesY[i4] - j - i3 < k1) {
                        k1 = mobMessagesY[i4] - j - i3;
                        flag = true;
                    }
            }
            mobMessagesY[i] = k1;
            surface.drawBoxTextColor(mobMessages[i], l, k1, 1, 0xffff00, 300);
        }
        for(int k = 0; k < anInt699; k++) {
            int i1 = anIntArray858[k];
            int l1 = anIntArray859[k];
            int k2 = anIntArray705[k];
            int j3 = anIntArray706[k];
            int l3 = (39 * k2) / 100;
            int j4 = (27 * k2) / 100;
            int k4 = l1 - j4;
            surface.spriteClip2(i1 - l3 / 2, k4, l3, j4, SPRITE_MEDIA + 9, 85);
            int l4 = (36 * k2) / 100;
            int i5 = (24 * k2) / 100;
            int mask = j3 == 183 ? ourPlayer.cape : Data.itemPictureMask[j3];
            surface.spriteClip4(i1 - l4 / 2, (k4 + j4 / 2) - i5 / 2, l4, i5, Data.itemInventoryPicture[j3] + SPRITE_ITEM, mask, 0, 0, false);
        }
        for(int j1 = 0; j1 < anInt718; j1++) {
            int i2 = healthBarX[j1];
            int l2 = healthBarY[j1];
            int k3 = healthBarPercentages[j1];
            surface.drawBoxAlpha(i2 - 15, l2 - 3, k3, 5, 65280, 192);
            surface.drawBoxAlpha((i2 - 15) + k3, l2 - 3, 30 - k3, 5, 0xff0000, 192);
        }
    }

    @Override
    protected final void method2() {
        if(errorUnableToLoad)
            return;
        if(memoryError)
            return;
        if(lastLoadedNull)
            return;
        try {
            loginTimer++;
            if(loggedIn == 0) {
                super.lastActionTimeout = 0;
                updateLoginScreen();
            }
            if(loggedIn == 1) {
                super.lastActionTimeout++;
                processGame();
            }
            super.lastMouseDownButton = 0;
            screenRotationTimer++;
            if(screenRotationTimer > 500) {
                screenRotationTimer = 0;
                int i = (int) (Math.random() * 4D);
                if((i & 1) == 1)
                    screenRotationX += anInt727;
                if((i & 2) == 2)
                    screenRotationY += anInt911;
            }
            if(screenRotationX < -50)
                anInt727 = 2;
            if(screenRotationX > 50)
                anInt727 = -2;
            if(screenRotationY < -50)
                anInt911 = 2;
            if(screenRotationY > 50)
                anInt911 = -2;
            if(anInt952 > 0)
                anInt952--;
            if(anInt953 > 0)
                anInt953--;
            if(anInt954 > 0)
                anInt954--;
            if(anInt955 > 0)
                anInt955--;
        } catch(OutOfMemoryError _ex) {
            garbageCollect();
            memoryError = true;
        }
    }

    @Override
    protected final void method4() {
        if(lastLoadedNull) {
            Graphics g = getGraphics();
            g.setColor(Color.black);
            g.fillRect(0, 0, 512, 356);
            g.setFont(new Font("Arial", 1, 16));
            g.setColor(Color.yellow);
            int i = 35;
            g.drawString("Sorry, an error has occured whilst loading " + GAME_NAME, 30, i);
            i += 50;
            g.setColor(Color.white);
            g.drawString("To fix this try the following (in order):", 30, i);
            i += 50;
            g.setColor(Color.white);
            g.setFont(new Font("Arial", 1, 12));
            g.drawString("1: Try closing ALL open web-browser windows, and reloading", 30, i);
            i += 30;
            g.drawString("2: Try clearing your web-browsers cache from tools->internet options", 30, i);
            i += 30;
            g.drawString("3: Try using a different game-world", 30, i);
            i += 30;
            g.drawString("4: Try rebooting your computer", 30, i);
            i += 30;
            g.drawString("5: Try selecting a different version of Java from the play-game menu", 30, i);
            return;
        }
        if(errorUnableToLoad) {
            Graphics g1 = getGraphics();
            g1.setColor(Color.black);
            g1.fillRect(0, 0, 512, 356);
            g1.setFont(new Font("Arial", 1, 20));
            g1.setColor(Color.white);
            g1.drawString("Error - unable to load game!", 50, 50);
            g1.drawString("To play " + GAME_NAME + " make sure you play from", 50, 100);
            g1.drawString(GAME_SITE, 50, 150);
            return;
        }
        if(memoryError) {
            Graphics g2 = getGraphics();
            g2.setColor(Color.black);
            g2.fillRect(0, 0, 512, 356);
            g2.setFont(new Font("Arial", 1, 20));
            g2.setColor(Color.white);
            g2.drawString("Error - out of memory!", 50, 50);
            g2.drawString("Close ALL unnecessary programs", 50, 100);
            g2.drawString("and windows before loading the game", 50, 150);
            g2.drawString(GAME_NAME + " needs about 48meg of spare RAM", 50, 200);
            return;
        }
        try {
            synchronized(sync_on_me) {
                if(shouldResize) {
                    shouldResize = false;
                    aGraphics936 = getGraphics();
                    windowWidth = resizeToW;
                    windowHeight = resizeToH;
                    gameWidth = windowWidth;
                    gameHeight = windowHeight;
                    surface.resize(windowWidth, windowHeight + 11, 4000, delegate.getContainerImpl());
                    scene.setCameraSize(windowWidth / 2, windowHeight / 2, windowWidth / 2, windowHeight / 2, windowWidth, cameraSizeInt);
                    GameUIs.overlay.stream().filter((o) -> (o.isVisible())).forEach((o) -> {
                        o.onResize(windowWidth, windowHeight);
                    });
                    System.out.println(windowWidth + " " + windowHeight);
                    drawGameMenu();
                    makeLoginMenus();
                    makeCharacterDesignMenu();
                    makeTest();
                }
            }
            /*
             * synchronized (sync_on_me) { if (shouldResize) { /* shouldResize =
             * false; aGraphics936 = getGraphics(); windowWidth = resizeToW;
             * windowHeight = resizeToH; gameWidth = windowWidth; gameHeight =
             * windowHeight; surface.resize(windowWidth, windowHeight + 11,
             * 4000, delegate.getContainerImpl(), scene, this);
             * for(GraphicalOverlay o : GameUIs.overlay) { if(o.isVisible) {
             * o.onResize(windowWidth , windowHeight); } } drawGameMenu();
             * makeLoginMenus(); makeCharacterDesignMenu(); makeTest();
             *
             *
             * shouldResize = false; aGraphics936 = getGraphics(); windowWidth =
             * resizeToW; windowHeight = resizeToH; gameWidth = windowWidth;
             * gameHeight = windowHeight; surface.resize(windowWidth,
             * windowHeight + 11, 4000, delegate.getContainerImpl());
             * scene.setCameraSize(windowWidth / 2, windowHeight / 2,
             * windowWidth / 2, windowHeight / 2, windowWidth, cameraSizeInt);
             * for (GraphicalOverlay o : GameUIs.overlay) { if (o.visible) {
             * o.onResize(windowWidth, windowHeight); } }
             * System.out.println(windowWidth + " " + windowHeight);
             * drawGameMenu(); makeLoginMenus(); makeCharacterDesignMenu();
             * makeTest(); } }
             */
            if(loggedIn == 0) {
                surface.loggedIn = false;
                drawLoginScreen();
            }
            if(loggedIn == 1) {
                surface.loggedIn = true;
                drawGame();
            }
        } catch(OutOfMemoryError _ex) {
            garbageCollect();
            memoryError = true;
        }
    }

    private void method62() {
        surface.interlace = false;
        surface.blackScreen();
        characterDesignMenu.drawMenu();
        int i = (gameWidth - 215) / 2;
        int j = gameHeight / 2 - 126;
        i += 116;
        j -= 25;
        surface.spriteClip3(i - 32 - 55, j, 64, 102, Data.animationNumber[character2Color], CLOTHES_COLORS[characterBottomColor]);
        surface.spriteClip4(i - 32 - 55, j, 64, 102, Data.animationNumber[characterBodyGender], CLOTHES_COLORS[characterTopColor], SKIN_COLORS[characterSkinColor], 0, false);
        surface.spriteClip4(i - 32 - 55, j, 64, 102, Data.animationNumber[characterHeadType], HAIR_COLORS[characterHairColor], SKIN_COLORS[characterSkinColor], 0, false);
        surface.spriteClip3(i - 32, j, 64, 102, Data.animationNumber[character2Color] + 6, CLOTHES_COLORS[characterBottomColor]);
        surface.spriteClip4(i - 32, j, 64, 102, Data.animationNumber[characterBodyGender] + 6, CLOTHES_COLORS[characterTopColor], SKIN_COLORS[characterSkinColor], 0, false);
        surface.spriteClip4(i - 32, j, 64, 102, Data.animationNumber[characterHeadType] + 6, HAIR_COLORS[characterHairColor], SKIN_COLORS[characterSkinColor], 0, false);
        surface.spriteClip3((i - 32) + 55, j, 64, 102, Data.animationNumber[character2Color] + 12, CLOTHES_COLORS[characterBottomColor]);
        surface.spriteClip4((i - 32) + 55, j, 64, 102, Data.animationNumber[characterBodyGender] + 12, CLOTHES_COLORS[characterTopColor], SKIN_COLORS[characterSkinColor], 0, false);
        surface.spriteClip4((i - 32) + 55, j, 64, 102, Data.animationNumber[characterHeadType] + 12, HAIR_COLORS[characterHairColor], SKIN_COLORS[characterSkinColor], 0, false);
        surface.drawSprite(0, windowHeight, SPRITE_MEDIA + 22);
        surface.draw(aGraphics936, 0, 0);
    }

    public final void method68(int i, int j, int k, int l, int i1, int j1, int k1) {
        int l1 = Data.itemInventoryPicture[i1] + SPRITE_ITEM;
        int i2 = i1 == 183 ? ourPlayer.cape : Data.itemPictureMask[i1];
        surface.spriteClip4(i, j, k, l, l1, i2, 0, 0, false);
    }

    public final void method71(int i, int j, int k, int l, int i1, int j1, int k1) {
        int l1 = anIntArray782[i1];
        int i2 = anIntArray923[i1];
        if(l1 == 0) {
            int j2 = 255 + i2 * 5 * gameWidth / 2;
            surface.drawCircle(i + k / 2, j + l / 2, 20 + i2 * 2, j2, 255 - i2 * 5);
        }
        if(l1 == 1) {
            int k2 = 0xff0000 + i2 * 5 * gameWidth / 2;
            surface.drawCircle(i + k / 2, j + l / 2, 10 + i2, k2, 255 - i2 * 5);
        }
    }

    private void method90() {
        try {
            soundFilesArchiveData = load("sounds" + VERSION_SOUNDS + ".mem", "Sound effects", 90);
            soundInputStream = new SoundInputStream();
        } catch(Throwable throwable) {
            System.out.println("Unable to init sounds:" + throwable);
        }
    }

    private void method98(int i, String s) {
        int j = objectX[i];
        int k = objectY[i];
        int l = j - ourPlayer.currentX / 128;
        int i1 = k - ourPlayer.currentY / 128;
        byte byte0 = 7;
        if(j >= 0 && k >= 0 && j < 96 && k < 96 && l > -byte0 && l < byte0 && i1 > -byte0 && i1 < byte0) {
            scene.removeModel(objectModelArray[i]);
            int j1 = Data.convertToModelInteger(s);
            GameModel model = gameModels[j1].copy();
            scene.addModel(model);
            model.setLight(true, 48, 48, -50, -10, -50);
            model.copyPosition(objectModelArray[i]);
            model.key = i;
            objectModelArray[i] = model;
        }
    }

    @Override
    public final void onResize(int width, int height) {
        Insets insets = delegate.getContainerImpl().getInsets();
        if((width - insets.left > 0) && (height - insets.top - 11 > 0)) {
            synchronized(sync_on_me) {
                resizeToW = width;
                resizeToH = height;
                shouldResize = true;
            }
        }
    }

    private void playSound(String s) {
        if(soundInputStream == null)
            return;

        if(configSoundEffects)
            return;

        soundInputStream.loadSoundFile(soundFilesArchiveData, Utility.getSoundFileStartOffset(s + ".pcm", soundFilesArchiveData), Utility.getSoundFileEndOffset(s + ".pcm", soundFilesArchiveData));
    }

    private void processGame() {
        if(systemUpdate > 1)
            systemUpdate--;
        sendPingPacketReadPacketData();
        if(logoutTimeout > 0)
            logoutTimeout--;
        /*
         * if (super.lastActionTimeout > 4500 && lastWalkTimeout == 0 &&
         * logoutTimeout == 0) { super.lastActionTimeout -= 500; logout();
         * return; }
         */
        if(ourPlayer.currentAnimation == 8 || ourPlayer.currentAnimation == 9)
            lastWalkTimeout = 500;
        if(lastWalkTimeout > 0)
            lastWalkTimeout--;
        if(showCharacterLookScreen) {
            drawCharacterLookScreen();
            return;
        }
        for(int i = 0; i < playerCount; i++) {
            Actor mob = playerArray[i];
            if(mob == null) {
                // System.out.println("WARNING: playerArray["+i+"] == null");
            } else {
                int k = (mob.waypointCurrent + 1) % 10;
                if(mob.waypointEndSprite != k) {
                    int i1 = -1;
                    int l2 = mob.waypointEndSprite;
                    int j4;
                    if(l2 < k)
                        j4 = k - l2;
                    else
                        j4 = (10 + k) - l2;
                    int j5 = 4;
                    if(j4 > 2)
                        j5 = (j4 - 1) * 4;
                    if(mob.waypointsX[l2] - mob.currentX > magicLoc * 3 || mob.waypointsY[l2] - mob.currentY > magicLoc * 3 || mob.waypointsX[l2] - mob.currentX < -magicLoc * 3 || mob.waypointsY[l2] - mob.currentY < -magicLoc * 3 || j4 > 8) {
                        mob.currentX = mob.waypointsX[l2];
                        mob.currentY = mob.waypointsY[l2];
                    } else {
                        if(mob.currentX < mob.waypointsX[l2]) {
                            mob.currentX += j5;
                            mob.stepCount++;
                            i1 = 2;
                        } else if(mob.currentX > mob.waypointsX[l2]) {
                            mob.currentX -= j5;
                            mob.stepCount++;
                            i1 = 6;
                        }
                        if(mob.currentX - mob.waypointsX[l2] < j5 && mob.currentX - mob.waypointsX[l2] > -j5)
                            mob.currentX = mob.waypointsX[l2];
                        if(mob.currentY < mob.waypointsY[l2]) {
                            mob.currentY += j5;
                            mob.stepCount++;
                            switch(i1) {
                                case -1:
                                    i1 = 4;
                                    break;
                                case 2:
                                    i1 = 3;
                                    break;
                                default:
                                    i1 = 5;
                                    break;
                            }
                        } else if(mob.currentY > mob.waypointsY[l2]) {
                            mob.currentY -= j5;
                            mob.stepCount++;
                            switch(i1) {
                                case -1:
                                    i1 = 0;
                                    break;
                                case 2:
                                    i1 = 1;
                                    break;
                                default:
                                    i1 = 7;
                                    break;
                            }
                        }
                        if(mob.currentY - mob.waypointsY[l2] < j5 && mob.currentY - mob.waypointsY[l2] > -j5)
                            mob.currentY = mob.waypointsY[l2];
                    }
                    if(i1 != -1)
                        mob.currentAnimation = i1;
                    if(mob.currentX == mob.waypointsX[l2] && mob.currentY == mob.waypointsY[l2])
                        mob.waypointEndSprite = (l2 + 1) % 10;
                } else {
                    mob.currentAnimation = mob.nextSprite;
                }
                if(mob.lastMessageTimeout > 0)
                    mob.lastMessageTimeout--;
                if(mob.itemBubbleDelay > 0)
                    mob.itemBubbleDelay--;
                if(mob.healthBarTimer > 0)
                    mob.healthBarTimer--;
                if(playerAliveTimeout > 0) {
                    playerAliveTimeout--;
                    if(playerAliveTimeout == 0)
                        displayMessage("You have been granted another life. Be more careful this time!", 3, null);
                    if(playerAliveTimeout == 0)
                        displayMessage("You retain your skills. Your objects land where you died", 3, null);
                }
            }
        }
        for(int j = 0; j < npcCount; j++) {
            Actor mob_1 = npcs[j];
            if(mob_1 == null)
                continue;
            int j1 = (mob_1.waypointCurrent + 1) % 10;
            if(mob_1.waypointEndSprite != j1) {
                int i3 = -1;
                int k4 = mob_1.waypointEndSprite;
                int k5;
                if(k4 < j1)
                    k5 = j1 - k4;
                else
                    k5 = (10 + j1) - k4;
                int l5 = 4;
                if(k5 > 2)
                    l5 = (k5 - 1) * 4;
                if(mob_1.waypointsX[k4] - mob_1.currentX > magicLoc * 3 || mob_1.waypointsY[k4] - mob_1.currentY > magicLoc * 3 || mob_1.waypointsX[k4] - mob_1.currentX < -magicLoc * 3 || mob_1.waypointsY[k4] - mob_1.currentY < -magicLoc * 3 || k5 > 8) {
                    mob_1.currentX = mob_1.waypointsX[k4];
                    mob_1.currentY = mob_1.waypointsY[k4];
                } else {
                    if(mob_1.currentX < mob_1.waypointsX[k4]) {
                        mob_1.currentX += l5;
                        mob_1.stepCount++;
                        i3 = 2;
                    } else if(mob_1.currentX > mob_1.waypointsX[k4]) {
                        mob_1.currentX -= l5;
                        mob_1.stepCount++;
                        i3 = 6;
                    }
                    if(mob_1.currentX - mob_1.waypointsX[k4] < l5 && mob_1.currentX - mob_1.waypointsX[k4] > -l5)
                        mob_1.currentX = mob_1.waypointsX[k4];
                    if(mob_1.currentY < mob_1.waypointsY[k4]) {
                        mob_1.currentY += l5;
                        mob_1.stepCount++;
                        switch(i3) {
                            case -1:
                                i3 = 4;
                                break;
                            case 2:
                                i3 = 3;
                                break;
                            default:
                                i3 = 5;
                                break;
                        }
                    } else if(mob_1.currentY > mob_1.waypointsY[k4]) {
                        mob_1.currentY -= l5;
                        mob_1.stepCount++;
                        switch(i3) {
                            case -1:
                                i3 = 0;
                                break;
                            case 2:
                                i3 = 1;
                                break;
                            default:
                                i3 = 7;
                                break;
                        }
                    }
                    if(mob_1.currentY - mob_1.waypointsY[k4] < l5 && mob_1.currentY - mob_1.waypointsY[k4] > -l5)
                        mob_1.currentY = mob_1.waypointsY[k4];
                }
                if(i3 != -1)
                    mob_1.currentAnimation = i3;
                if(mob_1.currentX == mob_1.waypointsX[k4] && mob_1.currentY == mob_1.waypointsY[k4])
                    mob_1.waypointEndSprite = (k4 + 1) % 10;
            } else {
                mob_1.currentAnimation = mob_1.nextSprite;
                if(mob_1.id == 43)
                    mob_1.stepCount++;
            }
            if(mob_1.lastMessageTimeout > 0)
                mob_1.lastMessageTimeout--;
            if(mob_1.itemBubbleDelay > 0)
                mob_1.itemBubbleDelay--;
            if(mob_1.healthBarTimer > 0)
                mob_1.healthBarTimer--;
        }
        if(mouseOverMenu != 2) {
            if(Surface.anInt346 > 0)
                anInt658++;
            if(Surface.anInt347 > 0)
                anInt658 = 0;
            Surface.anInt346 = 0;
            Surface.anInt347 = 0;
        }
        for(int l = 0; l < playerCount; l++) {
            Actor mob_2 = playerArray[l];
            if(mob_2 != null)
                if(mob_2.anInt176 > 0)
                    mob_2.anInt176--;
        }
        for(int l = 0; l < npcCount; l++) {
            Actor mob_2 = npcs[l];
            if(mob_2 != null)
                if(mob_2.anInt176 > 0)
                    mob_2.anInt176--;
        }
        if(cameraAutoAngleDebug) {
            if(lastAutoCameraRotatePlayerX - ourPlayer.currentX < -500 || lastAutoCameraRotatePlayerX - ourPlayer.currentX > 500 || lastAutoCameraRotatePlayerY - ourPlayer.currentY < -500 || lastAutoCameraRotatePlayerY - ourPlayer.currentY > 500) {
                lastAutoCameraRotatePlayerX = ourPlayer.currentX;
                lastAutoCameraRotatePlayerY = ourPlayer.currentY;
            }
        } else {
            if(lastAutoCameraRotatePlayerX - ourPlayer.currentX < -500 || lastAutoCameraRotatePlayerX - ourPlayer.currentX > 500 || lastAutoCameraRotatePlayerY - ourPlayer.currentY < -500 || lastAutoCameraRotatePlayerY - ourPlayer.currentY > 500) {
                lastAutoCameraRotatePlayerX = ourPlayer.currentX;
                lastAutoCameraRotatePlayerY = ourPlayer.currentY;
            }
            if(lastAutoCameraRotatePlayerX != ourPlayer.currentX)
                lastAutoCameraRotatePlayerX += (ourPlayer.currentX - lastAutoCameraRotatePlayerX) / (16 + (cameraHeight - 500) / 15);
            if(lastAutoCameraRotatePlayerY != ourPlayer.currentY)
                lastAutoCameraRotatePlayerY += (ourPlayer.currentY - lastAutoCameraRotatePlayerY) / (16 + (cameraHeight - 500) / 15);
            if(configAutoCameraAngle) {
                int k1 = cameraAutoAngle * 32;
                int j3 = k1 - cameraRotation;
                byte byte0 = 1;
                if(j3 != 0) {
                    cameraRotationBaseAddition++;
                    if(j3 > 128) {
                        byte0 = -1;
                        j3 = gameWidth / 2 - j3;
                    } else if(j3 > 0)
                        byte0 = 1;
                    else if(j3 < -128) {
                        byte0 = 1;
                        j3 = gameWidth / 2 + j3;
                    } else if(j3 < 0) {
                        byte0 = -1;
                        j3 = -j3;
                    }
                    cameraRotation += ((cameraRotationBaseAddition * j3 + 255) / gameWidth / 2) * byte0;
                    cameraRotation &= 0xff;
                } else {
                    cameraRotationBaseAddition = 0;
                }
            }
        }
        if(anInt658 > 20) {
            anInt658 = 0;
        }
        if(sleeping) {
            if(super.enteredText.length() > 0) {
                super.streamClass.addNewFrame(72);
                super.streamClass.addString(super.enteredText);
                super.streamClass.formatCurrentFrame();
                super.inputText = "";
                super.enteredText = "";
                sleepScreenMessage = "Please wait...";
            }
            if(super.lastMouseDownButton == 1 && super.mouseY > 275 && super.mouseY < 310 && super.mouseX > 56 && super.mouseX < 456) {
                super.streamClass.addNewFrame(72);
                super.streamClass.addString("-null-");
                super.streamClass.formatCurrentFrame();
                super.inputText = "";
                super.enteredText = "";
                sleepScreenMessage = "Please wait...";
            }
            super.lastMouseDownButton = 0;
            return;
        }
        if(mouseY > windowHeight - 4) {
            if((mouseX > windowWidth / 2 - 241) && (mouseX < windowWidth / 2 - 160) && (lastMouseDownButton == 1))
                messagesTab = 0;
            if((mouseX > windowWidth / 2 - 146) && (mouseX < windowWidth / 2 - 62) && (lastMouseDownButton == 1)) {
                messagesTab = 1;
                gameMenu.anIntArray187[generalChatHandle] = 999999;
            }
            if((mouseX > windowWidth / 2 - 41) && (mouseX < windowWidth / 2 + 39) && (lastMouseDownButton == 1)) {
                messagesTab = 2;
                gameMenu.anIntArray187[questChatHandle] = 999999;
            }
            if((mouseX > windowWidth / 2 + 59) && (mouseX < windowWidth / 2 + 139) && (lastMouseDownButton == 1)) {
                messagesTab = 3;
                gameMenu.anIntArray187[messagesHandleType6] = 999999;
            }
            lastMouseDownButton = 0;
            mouseDownButton = 0;
        }
        for(GraphicalOverlay overlay : GameUIs.overlay) {
            if(overlay.isVisible()) {
                if(overlay.onAction(mouseX, mouseY, mouseDownButton)) {
                    mouseDownButton = 0;
                    return;
                }
            }
        }
        gameMenu.updateActions(super.mouseX, super.mouseY, super.lastMouseDownButton, super.mouseDownButton);
        if((messagesTab > 0) && (mouseX >= 494) && (mouseY >= windowHeight - 66))
            lastMouseDownButton = 0;
        if(gameMenu.hasActivated(chatInputHandle)) {
            String s = gameMenu.getText(chatInputHandle);
            gameMenu.updateText(chatInputHandle, "");
            if(messages.isEmpty() || !messages.get(messages.size() - 1).equalsIgnoreCase(s)) {
                messages.add(s);
                currentChat = messages.size();
            } else if(messages.get(messages.size() - 1).equalsIgnoreCase(s))
                currentChat = messages.size();
            if(s.startsWith("/")) {
                if(!doBuiltInCommands(s.substring(1)))
                    sendCommand(s.substring(1));
            } else {
                int chatMessageLength = ChatMessage.stringToByteArray(s);
                sendChatMessage(ChatMessage.messageData, chatMessageLength);
                s = ChatMessage.byteToString(ChatMessage.messageData, 0, chatMessageLength);
                ourPlayer.lastMessageTimeout = 150;
                ourPlayer.currentMessage = s;
                displayMessage(getPrefix(ourPlayer.group) + ourPlayer.name + ": " + s, 2, ourPlayer);
            }
        }
        if(messagesTab == 0) {
            for(int l1 = 0; l1 < 5; l1++)
                if(messagesTimeout[l1] > 0)
                    messagesTimeout[l1]--;
        }
        if(playerAliveTimeout != 0)
            super.lastMouseDownButton = 0;
        if(showTradeWindow || showDuelWindow) {
            if(super.mouseDownButton != 0)
                mouseDownTime++;
            else
                mouseDownTime = 0;
            if(mouseDownTime > 500)
                itemIncrement += 100000;
            else if(mouseDownTime > 350)
                itemIncrement += 10000;
            else if(mouseDownTime > 250)
                itemIncrement += 1000;
            else if(mouseDownTime > 150)
                itemIncrement += 100;
            else if(mouseDownTime > 100)
                itemIncrement += 10;
            else if(mouseDownTime > 50)
                itemIncrement++;
            else if(mouseDownTime > 20 && (mouseDownTime & 5) == 0)
                itemIncrement++;
        } else {
            mouseDownTime = 0;
            itemIncrement = 0;
        }
        if(super.lastMouseDownButton == 1)
            mouseButtonClick = 1;
        else if(super.lastMouseDownButton == 2)
            mouseButtonClick = 2;
        scene.updateMouseCoords(super.mouseX, super.mouseY);
        super.lastMouseDownButton = 0;
        if(configAutoCameraAngle) {
            if(cameraRotationBaseAddition == 0 || cameraAutoAngleDebug) {
                if(super.keyLeftDown) {
                    cameraAutoAngle = cameraAutoAngle + 1 & 7;
                    super.keyLeftDown = false;
                    if(!zoomCamera) {
                        if((cameraAutoAngle & 1) == 0)
                            cameraAutoAngle = cameraAutoAngle + 1 & 7;
                        for(int i2 = 0; i2 < 8; i2++) {
                            if(enginePlayerVisible(cameraAutoAngle))
                                break;
                            cameraAutoAngle = cameraAutoAngle + 1 & 7;
                        }
                    }
                }
                if(super.keyRightDown) {
                    cameraAutoAngle = cameraAutoAngle + 7 & 7;
                    super.keyRightDown = false;
                    if(!zoomCamera) {
                        if((cameraAutoAngle & 1) == 0)
                            cameraAutoAngle = cameraAutoAngle + 7 & 7;
                        for(int j2 = 0; j2 < 8; j2++) {
                            if(enginePlayerVisible(cameraAutoAngle))
                                break;
                            cameraAutoAngle = cameraAutoAngle + 7 & 7;
                        }
                    }
                }
            }
        } else if(super.keyLeftDown)
            cameraRotation = cameraRotation + 2 & 0xff;
        else if(super.keyRightDown)
            cameraRotation = cameraRotation - 2 & 0xff;
        if(zoomCamera && cameraHeight > 850)
            cameraHeight -= 5;
        else if(!zoomCamera && cameraHeight < 750)
            cameraHeight += 4;
        if(actionPictureType > 0)
            actionPictureType--;
        else if(actionPictureType < 0)
            actionPictureType++;
        scene.method301(17);
        modelUpdatingTimer++;
        if(modelUpdatingTimer > 5) {
            modelUpdatingTimer = 0;
            modelFireLightningSpellNumber = (modelFireLightningSpellNumber + 1) % 3;
            modelTorchNumber = (modelTorchNumber + 1) % 4;
            modelClawSpellNumber = (modelClawSpellNumber + 1) % 5;
        }
        for(int k2 = 0; k2 < objectCount; k2++) {
            int l3 = objectX[k2];
            int l4 = objectY[k2];
            if(l3 >= 0 && l4 >= 0 && l3 < 96 && l4 < 96 && objectType[k2] == 74)
                objectModelArray[k2].rotate(1, 0, 0);
        }
        for(int i4 = 0; i4 < anInt892; i4++) {
            anIntArray923[i4]++;
            if(anIntArray923[i4] > 50) {
                anInt892--;
                for(int i5 = i4; i5 < anInt892; i5++) {
                    anIntArray944[i5] = anIntArray944[i5 + 1];
                    anIntArray757[i5] = anIntArray757[i5 + 1];
                    anIntArray923[i5] = anIntArray923[i5 + 1];
                    anIntArray782[i5] = anIntArray782[i5 + 1];
                }
            }
        }
    }

    public final void processNPCs(int x, int y, int k, int l, int index, int j1, int k1) {
        Actor npc = npcs[index];
        int l1 = npc.currentAnimation + (cameraRotation + 16) / 32 & 7;
        boolean flag = false;
        int i2 = l1;
        switch(i2) {
            case 5:
                i2 = 3;
                flag = true;
                break;
            case 6:
                i2 = 2;
                flag = true;
                break;
            case 7:
                i2 = 1;
                flag = true;
                break;
            default:
                break;
        }
        int j2 = i2 * 3 + WALK_ANIMATIONS[(npc.stepCount / Data.NPC_WALK_MODELS[npc.id]) % 4];
        if(npc.currentAnimation == 8) {
            i2 = 5;
            l1 = 2;
            flag = false;
            x -= (Data.npcCombatSprite[npc.id] * k1) / 100;
            j2 = i2 * 3 + COMBAT_ANIMATIONS_LEFT[(loginTimer / (Data.NPC_COMBAT_MODELS[npc.id] - 1)) % 8];
        } else if(npc.currentAnimation == 9) {
            i2 = 5;
            l1 = 2;
            flag = true;
            x += (Data.npcCombatSprite[npc.id] * k1) / 100;
            j2 = i2 * 3 + COMBAT_ANIMATIONS_RIGHT[(loginTimer / Data.NPC_COMBAT_MODELS[npc.id]) % 8];
        }
        for(int k2 = 0; k2 < 12; k2++) {
            int l2 = ANIMATIONS[l1][k2];
            int k3 = Data.npcAnimationCount[npc.id][l2];
            if(k3 >= 0) {
                int i4 = 0;
                int j4 = 0;
                int k4 = j2;
                if(flag && i2 >= 1 && i2 <= 3 && Data.animationHasF[k3] == 1)
                    k4 += 15;
                if(i2 != 5 || Data.animationHasA[k3] == 1) {
                    int l4 = k4 + Data.animationNumber[k3];
                    i4 = (i4 * k) / surface.imageFullWidth[l4];
                    j4 = (j4 * l) / surface.imageFullHeight[l4];
                    int i5 = (k * surface.imageFullWidth[l4]) / surface.imageFullWidth[Data.animationNumber[k3]];
                    i4 -= (i5 - k) / 2;
                    int color = Data.animationCharacterColor[k3];
                    int skinColor = 0;
                    switch(color) {
                        case 1:
                            color = Data.npcHairColor[npc.id];
                            skinColor = Data.npcSkinColor[npc.id];
                            break;
                        case 2:
                            color = Data.npcTopColor[npc.id];
                            skinColor = Data.npcSkinColor[npc.id];
                            break;
                        case 3:
                            color = Data.npcBottomColor[npc.id];
                            skinColor = Data.npcSkinColor[npc.id];
                            break;
                        default:
                            break;
                    }
                    surface.spriteClip4(x + i4, y + j4, i5, l, l4, color, skinColor, j1, flag);
                }
            }
        }
        if(npc.lastMessageTimeout > 0) {
            mobMessagesWidth[mobMessageCount] = surface.stringWidth(npc.currentMessage, 1) / 2;
            if(mobMessagesWidth[mobMessageCount] > 150)
                mobMessagesWidth[mobMessageCount] = 150;
            mobMessagesHeight[mobMessageCount] = (surface.stringWidth(npc.currentMessage, 1) / 300) * surface.stringHeight(1);
            mobMessagesX[mobMessageCount] = x + k / 2;
            mobMessagesY[mobMessageCount] = y;
            mobMessages[mobMessageCount++] = npc.currentMessage;
        }
        if(npc.currentAnimation == 8 || npc.currentAnimation == 9 || npc.healthBarTimer != 0) {
            if(npc.healthBarTimer > 0) {
                int i3 = x;
                if(npc.currentAnimation == 8)
                    i3 -= (20 * k1) / 100;
                else if(npc.currentAnimation == 9)
                    i3 += (20 * k1) / 100;
                healthBarX[anInt718] = i3 + k / 2;
                healthBarY[anInt718] = y;
                healthBarPercentages[anInt718++] = (npc.hitPointsCurrent * 30) / (npc.hitPointsBase > 0 ? npc.hitPointsBase : 1);
            }
            if(npc.healthBarTimer > 150) {
                int j3 = x;
                if(npc.currentAnimation == 8)
                    j3 -= (10 * k1) / 100;
                else if(npc.currentAnimation == 9)
                    j3 += (10 * k1) / 100;
                surface.drawSprite((j3 + k / 2) - 12, (y + l / 2) - 12, SPRITE_MEDIA + 12);
                surface.drawStringCentered(String.valueOf(npc.currentDamage), (j3 + k / 2) - 1, y + l / 2 + 5, 3, 0xffffff);
            }
        }
    }

    public final void processPlayers(int i, int j, int k, int l, int i1, int j1, int k1) {
        Actor mob = playerArray[i1];
        if(mob.colorBottomType == 255)
            return;
        int l1 = mob.currentAnimation + (cameraRotation + 16) / 32 & 7;
        boolean flag = false;
        int i2 = l1;
        switch(i2) {
            case 5:
                i2 = 3;
                flag = true;
                break;
            case 6:
                i2 = 2;
                flag = true;
                break;
            case 7:
                i2 = 1;
                flag = true;
                break;
            default:
                break;
        }
        int j2 = i2 * 3 + WALK_ANIMATIONS[(mob.stepCount / 6) % 4];
        if(mob.currentAnimation == 8) {
            i2 = 5;
            l1 = 2;
            flag = false;
            i -= (5 * k1) / 100;
            j2 = i2 * 3 + COMBAT_ANIMATIONS_LEFT[(loginTimer / 5) % 8];
        } else if(mob.currentAnimation == 9) {
            i2 = 5;
            l1 = 2;
            flag = true;
            i += (5 * k1) / 100;
            j2 = i2 * 3 + COMBAT_ANIMATIONS_RIGHT[(loginTimer / 6) % 8];
        }
        for(int k2 = 0; k2 < 12; k2++) {
            int l2 = ANIMATIONS[l1][k2];
            int l3 = mob.animationCount[l2] - 1;
            if(l3 >= 0) {
                int k4 = 0;
                int i5 = 0;
                int j5 = j2;
                if(flag && i2 >= 1 && i2 <= 3)
                    if(Data.animationHasF[l3] == 1)
                        j5 += 15;
                    else if(l2 == 4 && i2 == 1) {
                        k4 = -22;
                        i5 = -3;
                        j5 = i2 * 3 + WALK_ANIMATIONS[(2 + mob.stepCount / 6) % 4];
                    } else if(l2 == 4 && i2 == 2) {
                        k4 = 0;
                        i5 = -8;
                        j5 = i2 * 3 + WALK_ANIMATIONS[(2 + mob.stepCount / 6) % 4];
                    } else if(l2 == 4 && i2 == 3) {
                        k4 = 26;
                        i5 = -5;
                        j5 = i2 * 3 + WALK_ANIMATIONS[(2 + mob.stepCount / 6) % 4];
                    } else if(l2 == 3 && i2 == 1) {
                        k4 = 22;
                        i5 = 3;
                        j5 = i2 * 3 + WALK_ANIMATIONS[(2 + mob.stepCount / 6) % 4];
                    } else if(l2 == 3 && i2 == 2) {
                        k4 = 0;
                        i5 = 8;
                        j5 = i2 * 3 + WALK_ANIMATIONS[(2 + mob.stepCount / 6) % 4];
                    } else if(l2 == 3 && i2 == 3) {
                        k4 = -26;
                        i5 = 5;
                        j5 = i2 * 3 + WALK_ANIMATIONS[(2 + mob.stepCount / 6) % 4];
                    }
                if(i2 != 5 || Data.animationHasA[l3] == 1) {
                    try {
                        int k5 = j5 + Data.animationNumber[l3];
                        try {
                            k4 = (k4 * k) / surface.imageFullWidth[k5];
                            i5 = (i5 * l) / surface.imageFullHeight[k5];
                        } catch(Throwable localThrowable) {
                        }
                        int l5 = (k * surface.imageFullWidth[k5]) / surface.imageFullWidth[Data.animationNumber[l3]];
                        k4 -= (l5 - k) / 2;
                        int color = Data.animationCharacterColor[l3];
                        int skinColor = SKIN_COLORS[mob.colorSkinType];
                        if(color == 0xff0000) {
                            color = mob.cape;
                        }
                        switch(color) {
                            case 1:
                                color = HAIR_COLORS[mob.colorHairType];
                                break;
                            case 2:
                                color = CLOTHES_COLORS[mob.colorTopType];
                                break;
                            case 3:
                                color = CLOTHES_COLORS[mob.colorBottomType];
                                break;
                            default:
                                break;
                        }
                        surface.spriteClip4(i + k4, j + i5, l5, l, k5, color, skinColor, j1, flag);
                    } catch(ArrayIndexOutOfBoundsException aioobe) {
                        System.err.println("[ERROR] Updating: " + mob.name);
                    }
                }
            }
        }
        if(mob.lastMessageTimeout > 0) {
            mobMessagesWidth[mobMessageCount] = surface.stringWidth(mob.currentMessage, 1) / 2;
            if(mobMessagesWidth[mobMessageCount] > 150)
                mobMessagesWidth[mobMessageCount] = 150;
            mobMessagesHeight[mobMessageCount] = (surface.stringWidth(mob.currentMessage, 1) / 300) * surface.stringHeight(1);
            mobMessagesX[mobMessageCount] = i + k / 2;
            mobMessagesY[mobMessageCount] = j;
            mobMessages[mobMessageCount++] = mob.currentMessage;
        }
        if(mob.itemBubbleDelay > 0) {
            anIntArray858[anInt699] = i + k / 2;
            anIntArray859[anInt699] = j;
            anIntArray705[anInt699] = k1;
            anIntArray706[anInt699++] = mob.itemBubbleId;
        }
        if(mob.currentAnimation == 8 || mob.currentAnimation == 9 || mob.healthBarTimer != 0) {
            if(mob.healthBarTimer > 0) {
                int i3 = i;
                if(mob.currentAnimation == 8)
                    i3 -= (20 * k1) / 100;
                else if(mob.currentAnimation == 9)
                    i3 += (20 * k1) / 100;
                int i4 = (mob.hitPointsCurrent * 30) / mob.hitPointsBase;
                healthBarX[anInt718] = i3 + k / 2;
                healthBarY[anInt718] = j;
                healthBarPercentages[anInt718++] = i4;
            }
            if(mob.healthBarTimer > 150) {
                int j3 = i;
                if(mob.currentAnimation == 8)
                    j3 -= (10 * k1) / 100;
                else if(mob.currentAnimation == 9)
                    j3 += (10 * k1) / 100;
                surface.drawSprite((j3 + k / 2) - 12, (j + l / 2) - 12, SPRITE_MEDIA + 11);
                surface.drawStringCentered(String.valueOf(mob.currentDamage), (j3 + k / 2) - 1, j + l / 2 + 5, 3, 0xffffff);
            }
        }
        if(mob.isSkulled == 1 && mob.itemBubbleDelay == 0) {
            int k3 = j1 + i + k / 2;
            if(mob.currentAnimation == 8)
                k3 -= (20 * k1) / 100;
            else if(mob.currentAnimation == 9)
                k3 += (20 * k1) / 100;
            int j4 = (16 * k1) / 100;
            int l4 = (16 * k1) / 100;
            surface.spriteClip1(k3 - j4 / 2, j - l4 / 2 - (10 * k1) / 100, j4, l4, SPRITE_MEDIA + 13);
        }
    }

    private void removeDuelItems(int actionVariable, int actionVariable2, int actionType) {
        int currentDuelItemCount = 0;
        int removedCount = 0;
        if(actionType == 1234)
            if(Data.itemStackable[actionVariable] == 0)
                for(int c = 0; c < duelMyItemCount; c++)
                    if(duelMyItems[c] == actionVariable) {
                        duelMyItemsCount[c] = 0;
                        duelMyItemCount--;
                        for(int l2 = c; l2 < duelMyItemCount; l2++) {
                            duelMyItems[l2] = duelMyItems[l2 + 1];
                            duelMyItemsCount[l2] = duelMyItemsCount[l2 + 1];
                        }
                    }
        if(Data.itemStackable[actionVariable] == 0) {
            for(int c = 0; c < duelMyItemCount; c++)
                if(duelMyItems[c] == actionVariable)
                    if(actionVariable2 > duelMyItemsCount[c])
                        actionVariable2 = duelMyItemsCount[c];
        } else {
            for(int c = 0; c < duelMyItemCount; c++)
                if(duelMyItems[c] == actionVariable)
                    currentDuelItemCount++;
        }
        if(Data.itemStackable[actionVariable] == 0)
            for(int c = 0; c < duelMyItemCount; c++)
                if(Data.itemStackable[actionVariable] == 0 && duelMyItemsCount[c] > 0 && duelMyItems[c] == actionVariable) {
                    duelMyItemsCount[c] = duelMyItemsCount[c] - actionVariable2;
                    if(Data.itemStackable[duelMyItems[c]] == 0 && duelMyItemsCount[c] == 0 && duelMyItems[c] == actionVariable) {
                        duelMyItemCount--;
                        for(int l2 = c; l2 < duelMyItemCount; l2++) {
                            duelMyItems[l2] = duelMyItems[l2 + 1];
                            duelMyItemsCount[l2] = duelMyItemsCount[l2 + 1];
                        }
                    }
                }
        if(Data.itemStackable[actionVariable] == 1) {
            if(actionVariable2 > 12)
                actionVariable2 = 12;
            if(actionType == 1234)
                actionVariable2 = currentDuelItemCount;
            for(int c = 0; c < actionVariable2; c++)
                for(int duelCount = 0; duelCount < duelMyItemCount; duelCount++)
                    if(duelMyItems[duelCount] == actionVariable && removedCount != actionVariable2) {
                        duelMyItemCount--;
                        removedCount++;
                        for(int l22 = duelCount; l22 < duelMyItemCount; l22++) {
                            duelMyItems[l22] = duelMyItems[l22 + 1];
                            duelMyItemsCount[l22] = duelMyItemsCount[l22 + 1];
                        }
                    }
        }
        super.streamClass.addNewFrame(gameHeight / 2);
        super.streamClass.addByte(duelMyItemCount);
        for(int i3 = 0; i3 < duelMyItemCount; i3++) {
            super.streamClass.addShort(duelMyItems[i3]);
            super.streamClass.addInt(duelMyItemsCount[i3]);
        }
        super.streamClass.formatCurrentFrame();
        duelOpponentAccepted = false;
        duelMyAccepted = false;
    }

    private void removeTradeItems(int actionVariable, int actionVariable2, int actionType) {
        int currentTradeItemCount = 0;
        int removedCount = 0;
        if(actionType == 1234)
            if(Data.itemStackable[actionVariable] == 0)
                for(int c = 0; c < tradeMyItemCount; c++)
                    if(tradeMyItems[c] == actionVariable) {
                        tradeMyItemsCount[c] = 0;
                        tradeMyItemCount--;
                        for(int l2 = c; l2 < tradeMyItemCount; l2++) {
                            tradeMyItems[l2] = tradeMyItems[l2 + 1];
                            tradeMyItemsCount[l2] = tradeMyItemsCount[l2 + 1];
                        }
                    }
        if(Data.itemStackable[actionVariable] == 0) {
            for(int c = 0; c < tradeMyItemCount; c++)
                if(tradeMyItems[c] == actionVariable)
                    if(actionVariable2 > tradeMyItemsCount[c])
                        actionVariable2 = tradeMyItemsCount[c];
        } else {
            for(int c = 0; c < tradeMyItemCount; c++)
                if(tradeMyItems[c] == actionVariable)
                    currentTradeItemCount++;
        }
        if(actionType == 1234) {
            if(Data.itemStackable[actionVariable] == 0)
                for(int c = 0; c < tradeMyItemCount; c++)
                    if(tradeMyItems[c] == actionVariable) {
                        tradeMyItemCount--;
                        for(int l2 = c; l2 < tradeMyItemCount; l2++) {
                            tradeMyItems[l2] = tradeMyItems[l2 + 1];
                            tradeMyItemsCount[l2] = tradeMyItemsCount[l2 + 1];
                        }
                    }
        } else if(actionType != 1234)
            currentTradeItemCount = actionVariable2;
        if(Data.itemStackable[actionVariable] == 0)
            for(int c = 0; c < tradeMyItemCount; c++)
                if(Data.itemStackable[actionVariable] == 0 && tradeMyItemsCount[c] > 0 && tradeMyItems[c] == actionVariable) {
                    tradeMyItemsCount[c] = tradeMyItemsCount[c] - actionVariable2;
                    if(Data.itemStackable[tradeMyItems[c]] == 0 && tradeMyItemsCount[c] == 0 && tradeMyItems[c] == actionVariable) {
                        tradeMyItemCount--;
                        for(int l2 = c; l2 < tradeMyItemCount; l2++) {
                            tradeMyItems[l2] = tradeMyItems[l2 + 1];
                            tradeMyItemsCount[l2] = tradeMyItemsCount[l2 + 1];
                        }
                    }
                }
        if(Data.itemStackable[actionVariable] == 1) {
            if(actionVariable2 > 12)
                actionVariable2 = 12;
            if(actionType == 1234)
                actionVariable2 = currentTradeItemCount;
            for(int c = 0; c < actionVariable2; c++)
                for(int tradeCount = 0; tradeCount < tradeMyItemCount; tradeCount++)
                    if(tradeMyItems[tradeCount] == actionVariable && removedCount != actionVariable2) {
                        tradeMyItemCount--;
                        removedCount++;
                        for(int l22 = tradeCount; l22 < tradeMyItemCount; l22++) {
                            tradeMyItems[l22] = tradeMyItems[l22 + 1];
                            tradeMyItemsCount[l22] = tradeMyItemsCount[l22 + 1];
                        }
                    }
        }
        super.streamClass.addNewFrame(242);
        super.streamClass.addByte(tradeMyItemCount);
        for(int i3 = 0; i3 < tradeMyItemCount; i3++) {
            super.streamClass.addShort(tradeMyItems[i3]);
            super.streamClass.addInt(tradeMyItemsCount[i3]);
        }
        super.streamClass.formatCurrentFrame();
        tradeOtherAccepted = false;
        tradeWeAccepted = false;
    }

    @Override
    protected final void resetIntVars() {
        systemUpdate = 0;
        loginScreenNumber = 0;
        loggedIn = 0;
        logoutTimeout = 0;
    }

    private void resetLoginVars() {
        loggedIn = 0;
        loginScreenNumber = 0;
        currentUser = "";
        currentPass = "";
        playerCount = 0;
        npcCount = 0;
    }

    private void resetPrivateMessageStrings() {
        super.inputMessage = "";
        super.enteredMessage = "";
    }

    @Override
    protected final void resetVars() {
        systemUpdate = 0;
        combatStyle = 0;
        logoutTimeout = 0;
        loginScreenNumber = 0;
        loggedIn = 1;
        resetPrivateMessageStrings();
        surface.blackScreen();
        surface.draw(aGraphics936, 0, 0);
        for(int i = 0; i < objectCount; i++) {
            scene.removeModel(objectModelArray[i]);
            world.removeObject(objectX[i], objectY[i], objectType[i], objectID[i]);
        }
        for(int j = 0; j < wallObjectCount; j++) {
            scene.removeModel(wallObjectModel[j]);
            world.removeWallObject(wallObjectX[j], wallObjectY[j], wallObjectDirection[j], wallObjectType[j]);
        }
        objectCount = 0;
        wallObjectCount = 0;
        groundItemCount = 0;
        playerCount = 0;
        for(int k = 0; k < 4000; k++)
            mobArray[k] = null;
        for(int l = 0; l < 500; l++)
            playerArray[l] = null;
        npcCount = 0;
        for(int i1 = 0; i1 < 5000; i1++)
            npcRecordArray[i1] = null;
        for(int j1 = 0; j1 < 500; j1++)
            npcs[j1] = null;
        for(int k1 = 0; k1 < 50; k1++)
            prayerOn[k1] = false;
        mouseButtonClick = 0;
        super.lastMouseDownButton = 0;
        super.mouseDownButton = 0;
        showShop = false;
        GameUIs.overlay.get(0).setVisible(false);
        showPetInventory = false;
        super.friendsCount = 0;
    }

    public void saveImage(String tester, BufferedImage bi) {
        try {
            ImageIO.write(bi, "png", new File(tester + ".png"));
        } catch(IOException ex) {
        }
    }

    private boolean sendWalkCommand(int walkSectionX, int walkSectionY, int x1, int y1, int x2, int y2, boolean stepBoolean, boolean hasTargetEntity) {
        // todo: needs checking
        // System.out.println("Sent walk command");
        world.set(areaX, areaY);
        int stepCount = world.getStepCount(walkSectionX, walkSectionY, x1, y1, x2, y2, sectionXArray, sectionYArray, stepBoolean);
        // System.out.println("stepCount="+stepCount);
        if(stepCount == -1) {
            if(hasTargetEntity) {
                stepCount = 1;
                sectionXArray[0] = x1;
                sectionYArray[0] = y1;
            } else {
                return false;
            }
        }
        stepCount--;
        walkSectionX = sectionXArray[stepCount];
        walkSectionY = sectionYArray[stepCount];
        stepCount--;
        if(hasTargetEntity)
            super.streamClass.addNewFrame(157);
        else
            super.streamClass.addNewFrame(186);
        super.streamClass.addShort(walkSectionX + areaX);
        super.streamClass.addShort(walkSectionY + areaY);
        if(hasTargetEntity && stepCount == -1 && (walkSectionX + areaX) % 5 == 0)
            stepCount = 0;
        for(int currentStep = stepCount; currentStep >= 0 && currentStep > stepCount - 25; currentStep--) {
            super.streamClass.addByte(sectionXArray[currentStep] - walkSectionX);
            super.streamClass.addByte(sectionYArray[currentStep] - walkSectionY);
        }
        super.streamClass.formatCurrentFrame();
        actionPictureType = -24;
        actionPictureX = super.mouseX; // guessing the little red/yellow x that
        // appears when you click
        actionPictureY = super.mouseY;
        return true;
    }

    private boolean sendWalkCommandIgnoreTargetEntity(int walkSectionX, int walkSectionY, int x1, int y1, int x2, int y2, boolean stepBoolean, boolean hasTargetEntity) {
        world.set(areaX, areaY);
        int stepCount = world.getStepCount(walkSectionX, walkSectionY, x1, y1, x2, y2, sectionXArray, sectionYArray, stepBoolean);
        if(stepCount == -1)
            return false;
        stepCount--;
        walkSectionX = sectionXArray[stepCount];
        walkSectionY = sectionYArray[stepCount];
        stepCount--;
        if(hasTargetEntity)
            super.streamClass.addNewFrame(157);
        else
            super.streamClass.addNewFrame(186);
        super.streamClass.addShort(walkSectionX + areaX);
        super.streamClass.addShort(walkSectionY + areaY);
        if(hasTargetEntity && stepCount == -1 && (walkSectionX + areaX) % 5 == 0)
            stepCount = 0;
        for(int currentStep = stepCount; currentStep >= 0 && currentStep > stepCount - 25; currentStep--) {
            super.streamClass.addByte(sectionXArray[currentStep] - walkSectionX);
            super.streamClass.addByte(sectionYArray[currentStep] - walkSectionY);
        }
        super.streamClass.formatCurrentFrame();
        actionPictureType = -24;
        actionPictureX = super.mouseX;
        actionPictureY = super.mouseY;
        return true;
    }

    private void setPixelsAndAroundColor(int x, int y, int color) {
        surface.setPixelColor(x, y, color);
        surface.setPixelColor(x - 1, y, color);
        surface.setPixelColor(x + 1, y, color);
        surface.setPixelColor(x, y - 1, color);
        surface.setPixelColor(x, y + 1, color);
    }

    private void setupLoginScreenCamera() {
        int i = 0;
        byte byte0 = 50;
        byte byte1 = 50;
        world.populateSection(byte0 * 48 + 23, byte1 * 48 + 23, i);
        world.method428(gameModels);
        char c = '\u2600';
        char c1 = '\u1900';
        char c2 = '\u044C';
        char c3 = '\u0378';
        scene.clipFar3D = 4100;
        scene.clipFar2D = 4100;
        scene.fogZFalloff = 1;
        scene.fogZDistance = 4000;
        scene.setCamera(c, -world.getElevation(c, c1), c1, 912, c3, 0, c2 * 2);
        scene.finishCamera();
        surface.fadePixels();
        surface.drawBox(windowWidth / 2 - 256, windowHeight / 2 - 167, 512, 6, 0);
        for(int j = 6; j >= 1; j--)
            surface.drawBlurOut(0, j, windowWidth / 2 - 256, windowHeight / 2 - 167 + j, 512, 8);
        surface.drawBox(windowWidth / 2 - 256, windowHeight / 2 + 27, 512, 20, 0);
        for(int k = 6; k >= 1; k--)
            surface.drawBlurOut(0, k, windowWidth / 2 - 256, windowHeight / 2 + 27 - k, 512, 8);
        surface.drawSprite(15, 15, SPRITE_MEDIA + 10);
        surface.method229(SPRITE_LOGO, 0, 0, 512, 200);
        surface.drawWorld(SPRITE_LOGO);
        c = '\u2400';
        c1 = '\u2400';
        c2 = '\u044C';
        c3 = '\u0378';
        scene.clipFar3D = 4100;
        scene.clipFar2D = 4100;
        scene.fogZFalloff = 1;
        scene.fogZDistance = 4000;
        scene.setCamera(c, -world.getElevation(c, c1), c1, 912, c3, 0, c2 * 2);
        scene.finishCamera();
        surface.fadePixels();
        surface.drawBox(windowWidth / 2 - 256, windowHeight / 2 - 167, 512, 6, 0);
        for(int l = 6; l >= 1; l--)
            surface.drawBlurOut(0, l, windowWidth / 2 - 256, windowHeight / 2 - 167 + l, 512, 8);
        surface.drawBox(windowWidth / 2 - 256, windowHeight / 2 + 27, 512, 20, 0);
        for(int i1 = 6; i1 >= 1; i1--)
            surface.drawBlurOut(0, i1, windowWidth / 2 - 256, windowHeight / 2 + 27 - i1, 512, 8);
        surface.drawSprite(15, 15, SPRITE_MEDIA + 10);
        surface.method229(SPRITE_LOGO + 1, 0, 0, 512, 200);
        surface.drawWorld(SPRITE_LOGO + 1);
        for(int j1 = 0; j1 < 64; j1++) {
            scene.removeModel(world.aModelArrayArray598[0][j1]);
            scene.removeModel(world.aModelArrayArray580[1][j1]);
            scene.removeModel(world.aModelArrayArray598[1][j1]);
            scene.removeModel(world.aModelArrayArray580[2][j1]);
            scene.removeModel(world.aModelArrayArray598[2][j1]);
        }
        c = '\u2B80';
        c1 = '\u2880';
        c2 = '\u01F4';
        c3 = '\u0178';
        scene.clipFar3D = 4100;
        scene.clipFar2D = 4100;
        scene.fogZFalloff = 1;
        scene.fogZDistance = 4000;
        scene.setCamera(c, -world.getElevation(c, c1), c1, 912, c3, 0, c2 * 2);
        scene.finishCamera();
        surface.fadePixels();
        surface.drawBox(0, 0, 512, 6, 0);
        for(int k1 = 6; k1 >= 1; k1--)
            surface.drawBlurOut(0, k1, 0, k1, 512, 80);
        surface.drawBox(0, 194, 512, 20, 0);
        for(int l1 = 6; l1 >= 1; l1--)
            surface.drawBlurOut(0, l1, 0, 194, 512, 80);
        surface.drawSprite(15, 15, SPRITE_MEDIA + 10);
        surface.method229(SPRITE_MEDIA + 10, 0, 0, 512, 200);
        surface.drawWorld(SPRITE_MEDIA + 10);
    }

    private void shopActions() {
        int byte0 = gameWidth / 2 - 204;
        int byte1 = gameHeight / 2 - 123;
        int offsetX = byte0;
        int offsetY = byte1;
        if(mouseButtonClick != 0) {
            mouseButtonClick = 0;
            final int i = super.mouseX - (gameWidth - 411) / 2;
            final int j = super.mouseY - (gameHeight - 247) / 2;
            if((i >= 0) && (j >= 12) && (i < gameWidth / 2 + 206) && (j < gameHeight / 2 + 124)) {
                int k = 0;
                for(int i1 = 0; i1 < 5; i1++) {
                    for(int i2 = 0; i2 < 8; i2++) {
                        final int l2 = 7 + i2 * 49;
                        final int l3 = 28 + i1 * 34;
                        if(i > l2 && i < l2 + 49 && j > l3 && j < l3 + 34 && shopItems[k] != -1) {
                            selectedShopItemIndex = k;
                            selectedShopItemType = shopItems[k];
                        }
                        k++;
                    }
                }
                if(selectedShopItemIndex >= 0) {
                    final int j2 = shopItems[selectedShopItemIndex];
                    if(j2 != -1) {
                        if(super.mouseX >= offsetX + 220 && super.mouseY >= offsetY + 204 && super.mouseX < offsetX + 250 && super.mouseY <= offsetY + 215) {
                            super.streamClass.addNewFrame(178);
                            super.streamClass.addShort(shopItems[selectedShopItemIndex]);
                            super.streamClass.addInt(1);
                            super.streamClass.formatCurrentFrame();
                            return;
                        }
                        if(Data.itemStackable[j2] == 0) {
                            if(super.mouseX >= offsetX + 250 && super.mouseY >= offsetY + 204 && super.mouseX < offsetX + 280 && super.mouseY <= offsetY + 215) {
                                super.streamClass.addNewFrame(178);
                                super.streamClass.addShort(shopItems[selectedShopItemIndex]);
                                super.streamClass.addInt(50);
                                super.streamClass.formatCurrentFrame();
                                return;
                            }
                            if(super.mouseX >= offsetX + 280 && super.mouseY >= offsetY + 204 && super.mouseX < offsetX + 305 && super.mouseY <= offsetY + 215) {
                                super.streamClass.addNewFrame(178);
                                super.streamClass.addShort(shopItems[selectedShopItemIndex]);
                                super.streamClass.addInt(100);
                                super.streamClass.formatCurrentFrame();
                                return;
                            }
                            if(super.mouseX >= offsetX + 305 && super.mouseY >= offsetY + 204 && super.mouseX < offsetX + 335 && super.mouseY <= offsetY + 215) {
                                super.streamClass.addNewFrame(178);
                                super.streamClass.addShort(shopItems[selectedShopItemIndex]);
                                super.streamClass.addInt(500);
                                super.streamClass.formatCurrentFrame();
                                return;
                            }
                            if(super.mouseX >= offsetX + 335 && super.mouseY >= offsetY + 204 && super.mouseX < offsetX + 368 && super.mouseY <= offsetY + 215) {
                                super.inputText = "";
                                super.enteredText = "";
                                inputBoxType = 10;
                                inputID = j2;
                                return;
                            }
                        }
                        if(super.mouseX >= offsetX + 220 && super.mouseY >= byte1 + 229 && super.mouseX < offsetX + 250 && super.mouseY <= byte1 + 240) {
                            super.streamClass.addNewFrame(245);
                            super.streamClass.addShort(shopItems[selectedShopItemIndex]);
                            super.streamClass.addInt(1);
                            super.streamClass.formatCurrentFrame();
                            return;
                        }
                        if(Data.itemStackable[j2] == 0) {
                            if(super.mouseX >= offsetX + 250 && super.mouseY >= byte1 + 229 && super.mouseX < offsetX + 280 && super.mouseY <= byte1 + 240) {
                                super.streamClass.addNewFrame(245);
                                super.streamClass.addShort(shopItems[selectedShopItemIndex]);
                                super.streamClass.addInt(50);
                                super.streamClass.formatCurrentFrame();
                                return;
                            }
                            if(super.mouseX >= offsetX + 280 && super.mouseY >= byte1 + 229 && super.mouseX < offsetX + 305 && super.mouseY <= byte1 + 240) {
                                super.streamClass.addNewFrame(245);
                                super.streamClass.addShort(shopItems[selectedShopItemIndex]);
                                super.streamClass.addInt(100);
                                super.streamClass.formatCurrentFrame();
                                return;
                            }
                            if(super.mouseX >= offsetX + 305 && super.mouseY >= byte1 + 229 && super.mouseX < offsetX + 335 && super.mouseY <= byte1 + 240) {
                                super.streamClass.addNewFrame(245);
                                super.streamClass.addShort(shopItems[selectedShopItemIndex]);
                                super.streamClass.addInt(500);
                                super.streamClass.formatCurrentFrame();
                                return;
                            }
                            if(super.mouseX >= offsetX + 335 && super.mouseY >= byte1 + 229 && super.mouseX < offsetX + 368 && super.mouseY <= byte1 + 240) {
                                super.inputText = "";
                                super.enteredText = "";
                                inputBoxType = 11;
                                inputID = j2;
                            }
                        }
                    }
                }
            } else {
                super.streamClass.addNewFrame(129);
                super.streamClass.formatCurrentFrame();
                showShop = false;
            }
        }
    }

    private void showPetInventory() {
        int byte0 = 140;
        int byte1 = 40;
        surface.drawBox(byte0, byte1, 212, 12, 192);
        int l = 0x989898;
        surface.drawBoxAlpha(byte0, byte1 + 12, 212, 17, l, 160);
        surface.drawBoxAlpha(byte0, byte1 + 29, 8, gameHeight / 2, l, 160);
        surface.drawBoxAlpha(byte0 + 203, byte1 + 29, 9, gameHeight / 2, l, 160);
        surface.drawBoxAlpha(byte0, byte1 + 199, 212, 9, l, 160);
        surface.drawString("Pet Inventory", byte0 + 1, byte1 + 10, 1, 0xffffff);
        int index = 0;
        for(int k4 = 0; k4 < 5; k4++) {
            for(int l4 = 0; l4 < 4; l4++) {
                int j5 = byte0 + 7 + l4 * 49;
                int i6 = byte1 + 28 + k4 * 34;
                surface.drawBoxAlpha(j5 + 1, i6 + 1, 49, 34, Surface.convertRGBToLong(190, 190, 190), 160);
                surface.drawBoxEdge(j5 + 1, i6 + 1, 50, 35, 0);
                if(petInventoryItem[index] > 0) {
                    surface.spriteClip4(j5, i6, 48, 32, SPRITE_ITEM + Data.itemInventoryPicture[this.petInventoryItem[index]], Data.itemPictureMask[this.petInventoryItem[index]], 0, 0, false);
                    if(this.petInventoryQunant[index] > 1)
                        surface.drawBoxTextRight(insertCommas(String.valueOf(inventoryCount(this.petInventoryQunant[index]))), j5 + 47, i6 + 10, 1, 65535);
                }
                index++;
            }
        }
        int j1 = 0xffffff;
        if(super.mouseX > byte0 + 112 && super.mouseY >= byte1 && super.mouseX < byte0 + 217 && super.mouseY < byte1 + 12) {
            j1 = 0xff0000;
            if(mouseButtonClick != 0) {
                mouseButtonClick = 0;
                this.showPetInventory = false;
            }
        }
        surface.drawBoxTextRight("Close window", byte0 + 215, byte1 + 10, 1, j1);
        if(mouseX > 150 && mouseX < 345 && mouseY > 70 && mouseY < 240) {
            int currentInventorySlot = (mouseX - byte0) / 49 + ((mouseY - byte1) / 34) * 4 - 4;
            if(currentInventorySlot >= 0) {
                if(mouseButtonClick != 0) {
                    super.streamClass.addNewFrame(28);
                    super.streamClass.addShort(currentInventorySlot);
                    super.streamClass.formatCurrentFrame();
                    mouseButtonClick = 0;
                }
                if(this.petInventoryItem[currentInventorySlot] != -1)
                    surface.drawString("@whi@Take @lre@" + Data.itemName[this.petInventoryItem[currentInventorySlot]], 6, 14, 1, 0xffff00);
            }
        }
    }

    @Override
    protected final void startGame() {
        long i = 0;
        experienceArray = new long[200];
        for(int j = 0; j < experienceArray.length; j++) {
            int k = j + 1;
            long i1 = (long) ((double) k + 300D * Math.pow(2D, (double) k / 7D));
            i += i1;
            experienceArray[j] = (i & 0xfffffffc) / 4;
        }
        loadFonts();
        // loadJagex();
        GameNetworking.maxPacketReadCount = 1000;
        loadConfigFilter();
        if(lastLoadedNull)
            return;
        aGraphics936 = getGraphics();
        surface = new SurfaceSprite(windowWidth, windowHeight + 12, 4000, delegate.getContainerImpl());
        surface.client = this;
        surface.setBounds(0, 0, windowWidth, windowHeight + 12);
        makeTest();
        Menu.aBoolean220 = false;
        Menu.anInt221 = SPRITE_UTIL;
        spellMenu = new Menu(surface, 5);
        int l = surface.menuMaxWidth - 199;
        byte byte0 = 36;
        spellMenuHandle = spellMenu.addScrollableMenu(l, byte0 + 24, 196, 90, 1, 500, true);
        friendsMenu = new Menu(surface, 5);
        friendsMenuHandle = friendsMenu.addScrollableMenu(l, byte0 + 40, 196, 126, 1, 500, true);
        loadMedia();
        if(lastLoadedNull)
            return;
        loadEntity();
        if(lastLoadedNull)
            return;
        scene = new Scene(surface, 1500000, 1500000, 1000);
        scene.setCameraSize(windowWidth / 2, windowHeight / 2, windowWidth / 2, windowHeight / 2, windowWidth, cameraSizeInt);
        scene.clipFar3D = 2400;
        scene.clipFar2D = 2400;
        scene.fogZFalloff = 1;
        scene.fogZDistance = 2300;
        scene.method303(-50, -10, -50);
        world = new World(scene, surface, false);
        world.anInt588 = SPRITE_MEDIA;
        loadTextures();
        if(lastLoadedNull)
            return;
        loadModels();
        if(lastLoadedNull)
            return;
        if(member)
            method90();
        if(!lastLoadedNull) {
            drawLoadingBarText(100, "Starting game...");
            drawGameMenu();
            makeLoginMenus();
            makeCharacterDesignMenu();
            resetLoginVars();
            emptyGameWindowMethod();
            setupLoginScreenCamera();
        }
    }

    public String timeSince(long time) {
        int seconds = (int) ((System.currentTimeMillis() - time) / 1000);
        int minutes = seconds / 60;
        int hours = minutes / 60;
        int days = hours / 24;
        return days + " days, " + df.format(hours % 24) + ":" + df.format(minutes % 60) + ":" + df.format(seconds % 60);
    }

    public final void updateBankItems() {
        BankUI bank = (BankUI) (GameUIs.overlay.get(0));
        int bankIndex = bankSize;
        for(int j = 0; j < inventoryCount; j++) {
            if(bankIndex >= 197)
                break;
            int k = inventoryItems[j];
            boolean flag = false;
            for(int l = 0; l < bankIndex; l++) {
                if(bank.items.containsKey(l) && bank.items.get(l).getId() != k)
                    continue;
                flag = true;
                break;
            }
            if(!flag) {
                bank.items.put(bankIndex, bank.new BankItem(bankIndex, k, 0));
                bankIndex++;
            }
        }
        for(int i = bankIndex; i < 197; i++)
            bank.items.put(i, bank.new BankItem(i, -1, -1));
    }

    private void updateLoginScreen() {
        if(super.socketTimeout > 0)
            super.socketTimeout--;
        switch(loginScreenNumber) {
            case 0:
                menuWelcome.updateActions(super.mouseX, super.mouseY, super.lastMouseDownButton, super.mouseDownButton);
                if(menuWelcome.hasActivated(loginButtonNewUser))
                    loginScreenNumber = 1;
                if(menuWelcome.hasActivated(loginButtonExistingUser)) {
                    loginScreenNumber = 2;
                    menuLogin.updateText(loginStatusText, "Please enter your username and password");
                    menuLogin.updateText(loginUsernameTextBox, currentUser);
                    menuLogin.updateText(loginPasswordTextBox, currentPass);
                    menuLogin.setFocus(loginUsernameTextBox);
                }
                break;
            case 1:
                menuNewUser.updateActions(super.mouseX, super.mouseY, super.lastMouseDownButton, super.mouseDownButton);
                if(menuNewUser.hasActivated(newUserOkButton))
                    loginScreenNumber = 0;
                break;
            case 2:
                menuLogin.updateActions(super.mouseX, super.mouseY, super.lastMouseDownButton, super.mouseDownButton);
                if(menuLogin.hasActivated(loginCancelButton)) {
                    loginScreenNumber = 0;
                }
                if(menuLogin.hasActivated(loginUsernameTextBox))
                    menuLogin.setFocus(loginPasswordTextBox);
                if(menuLogin.hasActivated(loginPasswordTextBox) || menuLogin.hasActivated(loginOkButton)) {
                    currentUser = menuLogin.getText(loginUsernameTextBox);
                    currentPass = menuLogin.getText(loginPasswordTextBox);
                    login(currentUser, currentPass, false);
                }
                break;
            default:
                break;
        }
    }

    private void walkToBoundary(int actionX, int actionY, int direction) {
        if(direction == 0) {
            sendWalkCommand(regionX, regionY, actionX, actionY - 1, actionX, actionY, false, true);
            return;
        }
        if(direction == 1) {
            sendWalkCommand(regionX, regionY, actionX - 1, actionY, actionX, actionY, false, true);
            return;
        }

        sendWalkCommand(regionX, regionY, actionX, actionY, actionX, actionY, true, true);
    }

    private void walkToGroundItem(int walkSectionX, int walkSectionY, int x, int y, boolean coordsEqual) {
        if(!sendWalkCommandIgnoreTargetEntity(walkSectionX, walkSectionY, x, y, x, y, false, coordsEqual)) {
            sendWalkCommand(walkSectionX, walkSectionY, x, y, x, y, true, coordsEqual);
        }
    }

    private void walkToObject(int x, int y, int id, int type) {
        int i1;
        int j1;
        if(id == 0 || id == 4) {
            i1 = Data.objectWidth[type];
            j1 = Data.objectHeight[type];
        } else {
            j1 = Data.objectWidth[type];
            i1 = Data.objectHeight[type];
        }
        if(Data.objectType[type] == 2 || Data.objectType[type] == 3) {
            if(id == 0) {
                x--;
                i1++;
            }
            if(id == 2)
                j1++;
            if(id == 4)
                i1++;
            if(id == 6) {
                y--;
                j1++;
            }
            sendWalkCommand(regionX, regionY, x, y, (x + i1) - 1, (y + j1) - 1, false, true);
        } else {
            sendWalkCommand(regionX, regionY, x, y, (x + i1) - 1, (y + j1) - 1, true, true);
        }
    }

    public final static List<PacketHandler> PACKET_HANDLERS = new ArrayList<>();

    static {
        PACKET_HANDLERS.add(new PlayerPositionUpdate());
        PACKET_HANDLERS.add(new NPCPositionUpdate());
        PACKET_HANDLERS.add(new SleepIncorrectAnswer());
        PACKET_HANDLERS.add(new SleepFatigueUpdate());
        PACKET_HANDLERS.add(new FatigueUpdate());
    }
}
