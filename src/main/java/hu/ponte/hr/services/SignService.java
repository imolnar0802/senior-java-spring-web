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

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SignService {

    private PublicKey loadPubKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        log.debug("Load public key");
        var publicKeyBytes = getClass().getResourceAsStream("/config/keys/key.pub").readAllBytes();
        var pubKeyFactory = KeyFactory.getInstance("RSA");

        return pubKeyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
    }

    private PrivateKey loadPrivKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        log.debug("Load private key");
        var privKeyBytes = getClass().getResourceAsStream("/config/keys/key.private").readAllBytes();
        var privKeyFactory = KeyFactory.getInstance("RSA");

        return privKeyFactory.generatePrivate(new PKCS8EncodedKeySpec(privKeyBytes));
    }

    public String sign(byte[] data) throws Exception {
        log.debug("Start signing");
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(loadPrivKey());
        privateSignature.update(data);
        byte[] signature = privateSignature.sign();
        log.debug("Finish signing");

        if(verify(signature, data)) {
            log.info("Signature and verification are successful");
            return Base64.getEncoder().encodeToString(signature);
        } else {
            throw new SignatureException();
        }
    }

    private boolean verify(byte[] signature, byte[] data) throws Exception {
        log.debug("Start verifying");
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initVerify(loadPubKey());
        sign.update(data);
        log.debug("Finish verifying");
        return sign.verify(signature);
    }
}
