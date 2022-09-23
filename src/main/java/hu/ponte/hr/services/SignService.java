package hu.ponte.hr.services;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.stereotype.Service;

@Service
public class SignService {

    private PublicKey loadPubKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        var publicKeyBytes = getClass().getResourceAsStream("/config/keys/key.pub").readAllBytes();
        var pubKeyFactory = KeyFactory.getInstance("RSA");

        return pubKeyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
    }

    private PrivateKey loadPrivKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        var privKeyBytes = getClass().getResourceAsStream("/config/keys/key.private").readAllBytes();
        var privKeyFactory = KeyFactory.getInstance("RSA");

        return privKeyFactory.generatePrivate(new PKCS8EncodedKeySpec(privKeyBytes));
    }

    public String sign(String data) throws Exception {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(loadPrivKey());
        privateSignature.update(data.getBytes());
        byte[] signature = privateSignature.sign();

        if(verify(signature, data)) {
            return Base64.getEncoder().encodeToString(signature);
        } else {
            throw new SignatureException();
        }
    }

    private boolean verify(byte[] signature, String data) throws Exception {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initVerify(loadPubKey());
        sign.update(data.getBytes());
        return sign.verify(signature);
    }
}
