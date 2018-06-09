package net.swiftpk.client;

import net.swiftpk.client.bzip.DataEncryption;
import net.swiftpk.client.cache.ChatMessage;
import net.swiftpk.client.io.StreamClass;
import net.swiftpk.client.util.ImplementationDelegate;
import net.swiftpk.client.util.Utility;

import java.awt.*;
import java.io.IOException;
import java.security.SecureRandom;

import static net.swiftpk.client.util.GameConstants.*;

public class GameNetworking<Delegate_T extends ImplementationDelegate> extends GameManager<Delegate_T> {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static int maxPacketReadCount;

    public int blockChatMessages;

    public int blockDuelRequests;

    public int blockPrivateMessages;

    public int blockTradeRequests;

    public int friendsCount;

    public long friendsListLongs[];

    public int friendsListOnlineStatus[];

    public int ignoreListCount;

    public long ignoreListLongs[];

    long lastPing;

    byte packetData[];

    String password;

    public int port;

    int reconnectTries;

    public String server;

    public int socketTimeout;

    public StreamClass streamClass;

    public int userGroup;

    String username;

    public GameNetworking(Delegate_T c) {
        super(c);
        server = GAME_HOST;
        port = Integer.parseInt(GAME_PORT);
        username = "";
        password = "";
        packetData = new byte[5000];
        friendsListLongs = new long[200];
        friendsListOnlineStatus = new int[200];
        ignoreListLongs = new long[100];
    }

    protected final void addToFriendsList(String s) {
        streamClass.addNewFrame(213);
        streamClass.addLong(Utility.base37Encode(s));
        streamClass.formatCurrentFrame();
        long l = Utility.base37Encode(s);
        for(int i = 0; i < friendsCount; i++) {
            if(friendsListLongs[i] == l) {
                return;
            }
        }

        if(friendsCount < 100) {
            friendsListLongs[friendsCount] = l;
            friendsListOnlineStatus[friendsCount] = 0;
            friendsCount++;
        }
    }

    protected final void addToIgnoreList(String s) {
        long l = Utility.base37Encode(s);
        streamClass.addNewFrame(254);
        streamClass.addLong(l);
        streamClass.formatCurrentFrame();
        for(int i = 0; i < ignoreListCount; i++) {
            if(ignoreListLongs[i] == l) {
                return;
            }
        }

        if(ignoreListCount < 100) {
            ignoreListLongs[ignoreListCount++] = l;
        }
    }

    protected void cantLogout() {
    }

    private void readIncomingFrame(int command, int length) {
        //		System.out.println("pid=" + command+",\tlen="+length);
        if(command == 48) {
            String s = new String(packetData, 1, length - 1);
            handleServerMessage(s);
            return;
        }
        if(command == 222) {
            sendLogoutPacket();
            return;
        }
        if(command == 136) {
            cantLogout();
            return;
        }
        if(command == 249) {
            friendsCount = Utility.getUnsignedByte(packetData[1]);
            for(int k = 0; k < friendsCount; k++) {
                friendsListLongs[k] = Utility.getUnsignedLong(packetData, 2 + k * 9);
                friendsListOnlineStatus[k] = Utility.getUnsignedByte(packetData[10 + k * 9]);
            }

            reOrderFriendsListByOnlineStatus();
            return;
        }
        if(command == 25) {
            long friend = Utility.getUnsignedLong(packetData, 1);
            int status = packetData[9] & 0xff;
            for(int i2 = 0; i2 < friendsCount; i2++) {
                if(friendsListLongs[i2] == friend) {
                    if(friendsListOnlineStatus[i2] == 0 && status != 0) {
                        handleServerMessage("@pri@" + Utility.base37Decode(friend) + " has logged in");
                    }
                    if(friendsListOnlineStatus[i2] != 0 && status == 0) {
                        handleServerMessage("@pri@" + Utility.base37Decode(friend) + " has logged out");
                    }
                    friendsListOnlineStatus[i2] = status;
                    reOrderFriendsListByOnlineStatus();
                    return;
                }
            }

            friendsListLongs[friendsCount] = friend;
            friendsListOnlineStatus[friendsCount] = status;
            friendsCount++;
            reOrderFriendsListByOnlineStatus();
            return;
        }
        if(command == 2) {
            ignoreListCount = Utility.getUnsignedByte(packetData[1]);
            for(int i1 = 0; i1 < ignoreListCount; i1++) {
                ignoreListLongs[i1] = Utility.getUnsignedLong(packetData, 2 + i1 * 8);
            }

            return;
        }
        if(command == 158) {
            blockChatMessages = packetData[1];
            blockPrivateMessages = packetData[2];
            blockTradeRequests = packetData[3];
            blockDuelRequests = packetData[4];
            return;
        }
        if(command == 170) {
            long user = Utility.getUnsignedLong(packetData, 1);
            String s1 = ChatMessage.byteToString(packetData, 9, length - 9);
            handleServerMessage("@pri@" + Utility.base37Decode(user) + ": tells you " + s1);
        } else {
            handleIncomingPacket(command, length, packetData);
        }
    }

    private void emptyMethod() {
    }

    private void printConnectionLostMessages() {
        Graphics g = getGraphics();
        Font font = new Font("Helvetica", 1, 15);
        char c = '\u0200';
        char c1 = '\u0158';
        g.setColor(Color.black);
        g.fillRect(c / 2 - 140, c1 / 2 - 25, 280, 50);
        g.setColor(Color.white);
        g.drawRect(c / 2 - 140, c1 / 2 - 25, 280, 50);
        drawString(g, "Connection lost! Please wait...", font, c / 2, c1 / 2 - 10);
        drawString(g, "Attempting to re-establish", font, c / 2, c1 / 2 + 10);
    }

    protected byte[] getBytes(int[] i) {
        byte[] buf = new byte[i.length * 4];
        int off = 0;
        for(int n = 0; n < i.length; n++) {
            buf[off++] = (byte) (i[n] >> 24);
            buf[off++] = (byte) (i[n] >> 16);
            buf[off++] = (byte) (i[n] >> 8);
            buf[off++] = (byte) (i[n]);
        }
        return buf;
    }

    public long getUID() {
        return 0;
    }

    protected void handleIncomingPacket(int command, int length, byte abyte0[]) {
    }

    protected void handleServerMessage(String s) {
    }

    protected final void login(String user, String pass, boolean reconnecting) {
        if(socketTimeout > 0) {
            loginScreenPrint("Please wait...", "Connecting to server");
            try {
                Thread.sleep(2000L);
            } catch(Exception _ex) {
            }
            loginScreenPrint("Sorry! The server is currently full.", "Please try again later");
            return;
        }
        try {
            username = user;
            user = Utility.formatString(user);
            password = pass;
            pass = Utility.formatString(pass);
            if(user.trim().length() == 0) {
                loginScreenPrint("You must enter both a username", "and a password - Please try again");
                return;
            }
            if(reconnecting)
                printConnectionLostMessages();
            else
                loginScreenPrint("Please wait...", "Connecting to server");

            streamClass = new StreamClass(mudclient.makeSecureSocket(server, port));
            streamClass.maxFrameDecodeAttempts = maxPacketReadCount;
            long usernameHash = Utility.base37Encode(user);
            streamClass.addNewFrame(32); // Send session ID request
            streamClass.addByte((int) (usernameHash >> 16 & 0x1fL));
            streamClass.formatCurrentFrameAndFlushBuffer();
            long sessionID = streamClass.readLong();
            if(sessionID == 0L) {
                loginScreenPrint("Login server offline.", "Please try again in a few mins");
                return;
            }
            System.out.print("Session ID: " + sessionID);
            int sessionRotationKeys[] = new int[4];
            sessionRotationKeys[0] = SECURE_RANDOM.nextInt(99999999);
            //			sessionRotationKeys[0] = (int) (Math.random() * 99999999D);
            sessionRotationKeys[1] = SECURE_RANDOM.nextInt(99999999);
            //			sessionRotationKeys[1] = (int) (Math.random() * 99999999D);
            sessionRotationKeys[2] = (int) (sessionID >> 32);
            sessionRotationKeys[3] = (int) sessionID;
            DataEncryption dataEncryption = new DataEncryption(new byte[768]);
            dataEncryption.addInteger(sessionRotationKeys[0]);
            dataEncryption.addInteger(sessionRotationKeys[1]);
            dataEncryption.addInteger(sessionRotationKeys[2]);
            dataEncryption.addInteger(sessionRotationKeys[3]);
            dataEncryption.addLong(usernameHash);
            dataEncryption.addString(pass.trim());
            dataEncryption.encodeRSA();
//            byte[] byteBuffer = DataEncryption.encrypt();
            streamClass.addNewFrame(0); // player login
            streamClass.addByte(reconnecting ? 1 : 0);
            streamClass.addInt(Integer.parseInt(GAME_VERSION));
            streamClass.addBytes(dataEncryption.dataBuffer, 0, dataEncryption.caret);
            streamClass.formatCurrentFrameAndFlushBuffer();
            int loginResponse = streamClass.readInputStream();
            System.out.println(" - Login Response:" + loginResponse);
            if(loginResponse == 25) {
                userGroup = 2; // admin
                reconnectTries = 0;
                resetVars();
                return;
            }
            if(loginResponse == 24) {
                userGroup = 1; // mod
                reconnectTries = 0;
                resetVars();
                return;
            }
            if(loginResponse == 0) {
                userGroup = 0;
                reconnectTries = 0;
                resetVars();
                return;
            }
            if(loginResponse == 1) {
                reconnectTries = 0;
                emptyMethod();
                return;
            }
            if(reconnecting) {
                //				user = "";
                //				pass = "";
                resetIntVars();
                return;
            }
            if(loginResponse == -1) {
                loginScreenPrint("Error unable to login.", "Server timed out");
                return;
            }
            if(loginResponse == 3) {
                loginScreenPrint("Invalid username or password.", "Try again, or create a new account");
                return;
            }
            if(loginResponse == 4) {
                loginScreenPrint("That username is already logged in.", "Wait 60 seconds then retry");
                return;
            }
            if(loginResponse == 5) {
                loginScreenPrint("The client has been updated.", "Please reload this page");
                return;
            }
            if(loginResponse == 6) {
                loginScreenPrint("You may only use 1 character at once.", "Your account is already in use");
                return;
            }
            if(loginResponse == 7) {
                loginScreenPrint("Login attempts exceeded!", "Please try again in 5 minutes");
                return;
            }
            if(loginResponse == 8) {
                loginScreenPrint("Error unable to login.", "Server rejected session");
                return;
            }
            if(loginResponse == 9) {
                loginScreenPrint("Error unable to login.", "Loginserver rejected session");
                return;
            }
            if(loginResponse == 10) {
                loginScreenPrint("That username is already in use.", "Wait 60 seconds then retry");
                return;
            }
            if(loginResponse == 11) {
                loginScreenPrint("Account temporarily disabled.", "");
                return;
            }
            if(loginResponse == 12) {
                loginScreenPrint("Account permanently disabled.", "");
                return;
            }
            if(loginResponse == 14) {
                loginScreenPrint("Sorry! This world is currently full.", "Please try a different world");
                socketTimeout = 1500;
                return;
            }
            if(loginResponse == 15) {
                loginScreenPrint("You need a members account", "to login to this world");
                return;
            }
            if(loginResponse == 16) {
                loginScreenPrint("Error - no reply from loginserver.", "Please try again");
                return;
            }
            if(loginResponse == 17) {
                loginScreenPrint("Error - failed to decode profile.", "Contact customer support");
                return;
            }
            if(loginResponse == 20) {
                loginScreenPrint("Error - loginserver mismatch", "Please try a different world");
                return;
            } else {
                loginScreenPrint("Error unable to login.", "Unrecognised response code");
                return;
            }
        } catch(IOException | NumberFormatException exception) {
            System.out.println(String.valueOf(exception));
        }
        if(reconnectTries > 0) {
            try {
                Thread.sleep(2500L);
            } catch(Exception _ex) {
            }
            reconnectTries--;
            login(username, password, reconnecting);
        }
        if(reconnecting) {
            username = "";
            password = "";
            resetIntVars();
        } else {
            loginScreenPrint("Sorry! Unable to connect.", "Check internet settings or try another world");
        }
    }

    protected void loginScreenPrint(String s, String s1) {
    }

    protected void lostConnection() {
        System.out.println("Lost connection");
        reconnectTries = 10;
        login(username, password, true);
    }

    protected final void removeFromFriends(long l) {
        streamClass.addNewFrame(99);
        streamClass.addLong(l);
        streamClass.formatCurrentFrame();
        for(int i = 0; i < friendsCount; i++) {
            if(friendsListLongs[i] != l) {
                continue;
            }
            friendsCount--;
            for(int j = i; j < friendsCount; j++) {
                friendsListLongs[j] = friendsListLongs[j + 1];
                friendsListOnlineStatus[j] = friendsListOnlineStatus[j + 1];
            }

            break;
        }

        handleServerMessage("@pri@" + Utility.base37Decode(l) + " has been removed from your friends list");
    }

    protected final void removeFromIgnoreList(long l) {
        streamClass.addNewFrame(173);
        streamClass.addLong(l);
        streamClass.formatCurrentFrame();
        for(int i = 0; i < ignoreListCount; i++) {
            if(ignoreListLongs[i] == l) {
                ignoreListCount--;
                for(int j = i; j < ignoreListCount; j++) {
                    ignoreListLongs[j] = ignoreListLongs[j + 1];
                }

                return;
            }
        }

    }

    private void reOrderFriendsListByOnlineStatus() {
        boolean flag = true;
        while(flag) {
            flag = false;
            for(int i = 0; i < friendsCount - 1; i++) {
                if(friendsListOnlineStatus[i] < friendsListOnlineStatus[i + 1]) {
                    int j = friendsListOnlineStatus[i];
                    friendsListOnlineStatus[i] = friendsListOnlineStatus[i + 1];
                    friendsListOnlineStatus[i + 1] = j;
                    long l = friendsListLongs[i];
                    friendsListLongs[i] = friendsListLongs[i + 1];
                    friendsListLongs[i + 1] = l;
                    flag = true;
                }
            }

        }
    }

    protected void resetIntVars() {
    }

    protected void resetVars() {
    }

    protected final void sendChatMessage(byte abyte0[], int i) {
        streamClass.addNewFrame(174);
        streamClass.addBytes(abyte0, 0, i);
        streamClass.formatCurrentFrame();
    }

    protected final void sendCommand(String s) {
        streamClass.addNewFrame(120);
        streamClass.addString(s);
        streamClass.formatCurrentFrame();
    }

    protected final void sendLogoutPacket() {
        if(streamClass != null) {
            try {
                streamClass.addNewFrame(82);
                streamClass.formatCurrentFrameAndFlushBuffer();
            } catch(IOException _ex) {
            }
        }
        username = "";
        password = "";
        resetIntVars();
    }

    protected final void sendPingPacketReadPacketData() {
        long start = System.currentTimeMillis();
        if(streamClass.containsData())
            lastPing = start;
        if(start - lastPing > 5000L) {
            lastPing = start;
            streamClass.addNewFrame(5);
            streamClass.formatCurrentFrame();
            mudclient.PING_SENT = System.nanoTime();
        }
        try {
            streamClass.flushBufferIfCountOver(20);
        } catch(IOException _ex) {
            lostConnection();
            return;
        }
        int packetLength = streamClass.readFrameIntoBuffer(packetData);
        if(packetLength > 0) {
            readIncomingFrame(packetData[0] & 0xFF, packetLength);
            // checkIncomingPacket(streamClass.decryptIncomingCommand(newData[0],
            // currentOffset), newData);
        }
    }

    protected final void sendPrivateMessage(long user, byte message[], int messageLength) {
        streamClass.addNewFrame(192);
        streamClass.addLong(user);
        streamClass.addBytes(message, 0, messageLength);
        streamClass.formatCurrentFrame();
    }

    protected final void sendUpdatedPrivacyInfo(int chatMessages, int privateMessages, int tradeRequests, int duelRequests) {
        streamClass.addNewFrame(191);
        streamClass.addByte(chatMessages);
        streamClass.addByte(privateMessages);
        streamClass.addByte(tradeRequests);
        streamClass.addByte(duelRequests);
        streamClass.formatCurrentFrame();
    }
}
