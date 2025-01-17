package com.github.steveice10.mc.protocol.packet.login.client;

import com.github.steveice10.mc.protocol.util.CryptUtil;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import com.github.steveice10.packetlib.packet.Packet;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;

@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EncryptionResponsePacket implements Packet {
    private @NonNull byte sharedKey[];
    private @NonNull byte verifyToken[];

    public EncryptionResponsePacket(PublicKey publicKey, SecretKey secretKey, byte verifyToken[]) {
        this.sharedKey = CryptUtil.encryptData(publicKey, secretKey.getEncoded());
        this.verifyToken = CryptUtil.encryptData(publicKey, verifyToken);
    }

    public SecretKey getSecretKey(PrivateKey privateKey) {
        return CryptUtil.decryptSharedKey(privateKey, this.sharedKey);
    }

    public byte[] getVerifyToken(PrivateKey privateKey) {
        return CryptUtil.decryptData(privateKey, this.verifyToken);
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.sharedKey = in.readBytes(in.readVarInt());
        this.verifyToken = in.readBytes(in.readVarInt());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.sharedKey.length);
        out.writeBytes(this.sharedKey);
        out.writeVarInt(this.verifyToken.length);
        out.writeBytes(this.verifyToken);
    }

    @Override
    public boolean isPriority() {
        return true;
    }
}
