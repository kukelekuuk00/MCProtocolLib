package com.github.steveice10.mc.protocol.packet.login.client;

import com.github.steveice10.mc.protocol.packet.PacketTest;
import com.github.steveice10.mc.protocol.util.CryptUtil;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.util.Random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class EncryptionResponsePacketTest extends PacketTest {
    private KeyPair keyPair;
    private SecretKey secretKey;
    private EncryptionResponsePacket packet;
    private byte[] verifyToken;

    @Before
    public void setup() {
        this.keyPair = CryptUtil.generateKeyPair();
        this.secretKey = CryptUtil.generateSharedKey();
        this.verifyToken = new byte[4];
        new Random().nextBytes(this.verifyToken);

        this.packet = new EncryptionResponsePacket(this.keyPair.getPublic(), this.secretKey, this.verifyToken);
        this.setPackets(this.packet);
    }

    @Test
    public void testEncryptionResponsePacketGetters() {
        assertEquals("Secret key does not match.", this.secretKey, this.packet.getSecretKey(this.keyPair.getPrivate()));
        assertArrayEquals("Verify token does not match.", this.verifyToken, this.packet.getVerifyToken(this.keyPair.getPrivate()));
    }
}
