package net.swiftpk.client.bzip;

import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

//import org.apache.commons.codec.binary.Base64;

public class DataEncryption {
	private static final BigInteger RSA_EXPONENT = new BigInteger("454622801386996070730420189913704739139837458274841667756026234442778738368515403834632717067934383238560264359746089311850616773466098320166705780188737865312489243239291697328887950454833756208077956293654450928079224494019822711705072548622098412055754088851476331699343663386235708622993494756508888975682023951458614565215707290489166772740965963256419551165692286608544871778125468415105725657407728156476777486999578141137174033793197386748499787411558929588287080949049159740034934938788129044990217532589038434450229848732860396848147421726679896630607562036045622687696782072310626586390884455399248600746762627949064867834977927646503643838023778135328345220421556161846822228746962649151989931426216144976218011187561027125411605506242871895086422154969635142801529694227138524823102046384819114776312538176887347125882009604857260622022027925615892396588142530719858402596887212887178472644793488721787897851062289991565904908900626760308303253892155524480995602008011058767865168568639504602248567303593370566054055447339705428214202507003228106882738900768203545836688609361242750251163255776613228058205026463517994481478581170670246717848314041431158091495960801047974224755876586502598864701491096009381985546547337");
	private static final BigInteger RSA_MODULUS = new BigInteger("655028054189320147340461668922072919749966160231916315400122221838190572421607304473093366345750189107725838044353769694988804733768982101098296784062092753917581748122045034110177657236670800554875133136790648560331257765844874042425729449012314602870057054947762314325906994875781378744473168492823667474471224733083334041773985336186859493383974184747490693900562936058735071768477058442295165982766319003405187806335323006071877715568834212570551418153967274561262726107706865191743005685592022391498561954202524293050808677549482872864830515179371451749317082607835005483987566839812095765418433602413206624961332432934373631599999714199300115284431149778887904407465207237006004921893147586447659464617305790543554237122668688369744680208115226745243507069459240090322487946619630520524966378162249673913777058196742647223153177820426565764947278267572220799267884950862059253126097203441717248722907482420362402072559895453824132576525864028329948449438437050025071260822959703524409919770745638170008311962135144902992059821839534839748921782592225282551345901522935814474182646980999781219074842338957527975375958543333371834454170429279799301189734855898259297396107405056565475420108121740269714448003425571195105063424927");
private static final PublicKey RSA_KEY = getPublicKey("public.key");
    private static String getKey(String filename) throws IOException {
        // Read key from file
        String strKeyPEM = "";
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = br.readLine()) != null) {
            strKeyPEM += line + "n";
        }
        br.close();
        return strKeyPEM;
    }
    /**
     * Constructs a public key (RSA) from the given file
     *
     * @param filename PEM Public Key
     * @return RSA Public Key
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public static RSAPublicKey getPublicKey(String filename) {
        String publicKeyPEM = null;
        try {
            publicKeyPEM = getKey(filename);
        } catch(IOException e) {
            e.printStackTrace();
        }
        try {
            return getPublicKeyFromString(publicKeyPEM);
        } catch(IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Constructs a public key (RSA) from the given string
     *
     * @param key PEM Public Key
     * @return RSA Public Key
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public static RSAPublicKey getPublicKeyFromString(String key) throws IOException, GeneralSecurityException {
        String publicKeyPEM = key;

        // Remove the first and last lines
        publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----n", "");
        publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");

        // Base64 decode data
        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);

        KeyFactory kf = KeyFactory.getInstance("RSA");
        RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));
        return pubKey;
    }

    public int caret;
	public byte[] dataBuffer;

	public DataEncryption(byte[] buffer) {
		dataBuffer = buffer;
		caret = 0;
	}

	public void addLong(long l) {
		addInteger((int) (l >> 32));
		addInteger((int) (l & -1L));
	}

	public void addInteger(int i) {
		addByte((byte) (i >> 24));
		addByte((byte) (i >> 16));
		addShort(i);
//		addByte((byte) (i >> 8));
//		addByte((byte) i);
	}

	public void addShort(int i) {
		addByte((byte) (i >> 8));
		addByte((byte) i);
	}

	public void addByte(int i) {
		dataBuffer[caret++] = (byte) i;
	}

	private void addBytes(byte[] buffer, int i, int j) {
		for(int k = i; k < i + j; k++)
			addByte(buffer[k]);
	}

	public void addString(String s) {
		for(byte b : s.getBytes())
			addByte(b);
		addByte(0xA);
	}

	public void encodeRSA() {

        int length =caret;
        caret = 0;
        byte[] buffer = new byte[length];
        getBytes(buffer, 0, length);
        try {
            buffer = encrypt(buffer, RSA_KEY);
        } catch(GeneralSecurityException e) {
            e.printStackTrace();
            System.out.println("Error IO-1!  Please report this to a staff member as soon as possible.");
        }

        addBytes(buffer, 0, buffer.length);
/*

        int length = caret;
        caret = 0;
        byte[] buffer = new byte[length];
        getBytes(buffer, 0, length);
        byte[] rsaBuffer = buffer;
        BigInteger bigInteger = new BigInteger(buffer);
        BigInteger rsa = bigInteger.modPow(RSA_EXPONENT, RSA_MODULUS);
        rsaBuffer = rsa.toByteArray();
        caret = 0;
        addShort(rsaBuffer.length);
        addBytes(rsaBuffer, 0, rsaBuffer.length);***/
	}

    /**
     * Encrypts the text with the public key (RSA)
     *
     * @param rawText data to be encrypted
     * @param publicKey
     * @return
     * @throws GeneralSecurityException
     */
    private static byte[] encrypt(byte[] rawText, PublicKey publicKey) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(rawText);
    }

    public int getShort() {
		return ((getByte() & 0xFF) << 8) + (getByte() & 0xFF);
	}

	public int getInteger() {
		return ((getByte() & 0xFF) << 24) +
				((getByte() & 0xFF) << 16) +
				getShort();
	}

	public byte getByte() {
		return (byte) (dataBuffer[caret++] & 0xFF);
	}

	private void getBytes(byte[] buffer, int i, int j) {
		for(int k = i; k < i + j; k++) {
			buffer[k] = getByte();
		}
	}

}
