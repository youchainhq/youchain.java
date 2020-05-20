package cc.youchain.crypto;

import cc.youchain.utils.Numeric;

import java.math.BigInteger;

public class TestKeys {

    public TestKeys() {}

    public static final String PRIVATE_KEY_STRING =
            "0xae6cbb33a5567638891a5413b8f3bf49288a24a5cd55d4fa6bd905f4a497d6a8";
    public static final String PUBLIC_KEY_STRING =
            "0x79fc39db20e8d73745f62398d889056cd8be00385d7db85220e3c83718f9dbd1" +
                    "524d94c0a7fb6ecc2f1f36e78359c235f1a66fc718acbdda00fec5bbf9483f86";
    public static final String ADDRESS = "0x0b42eb76113d9ede60c68b9e6832f65ff8a78bbf";
    public static final String ADDRESS_NO_PREFIX = Numeric.cleanHexPrefix(ADDRESS);
    public static final String PASSWORD = "Test_Password_123456";
    public static final String MNEMONIC = "emotion vote rabbit company lobster ecology system estate plunge together sort brick";
    public static final BigInteger PRIVATE_KEY = Numeric.toBigInt(PRIVATE_KEY_STRING);
    public static final BigInteger PUBLIC_KEY = Numeric.toBigInt(PUBLIC_KEY_STRING);
    public static final ECKeyPair KEY_PAIR = new ECKeyPair(PRIVATE_KEY, PUBLIC_KEY);
    public static final Credentials CREDENTIALS = Credentials.create(KEY_PAIR);
    public static final String AES_128_CTR =
            "{\"address\":\"6425d266cd3fcf3dd440d739d834737b85a40df6\",\"id\":\"a0a28605-9563-48d1-818e-b52b8238c51a\",\"version\":3,\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"03fe9b3edf4a5413b1856100eaacc7dbad7e0c14f9896fdd5145aa08349af816\",\"cipherparams\":{\"iv\":\"0aa13833fd04aa5f0f2613f57338d455\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":4096,\"p\":6,\"r\":8,\"salt\":\"3ace4de8e706088a545bce1235642f723ad7c937884f1385bbcc62db530514e0\"},\"mac\":\"b68580681e7401c18d91d15f07af6d87d756101ee3b5f5a43f5cd9fef5372457\"}}";
    public static final String SCRYPT =
            "{\"address\":\"88189a9d3b0ccbc370e67b91211abf7f33fcf6d5\",\"id\":\"f06b3288-9b48-4e7a-8aef-ae4483e8fbcc\",\"version\":3,\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"9b18759bcb4ae6b72abf88a56ecf4b8e8d6687b85625669bf0a6df87521b9217\",\"cipherparams\":{\"iv\":\"ae78c63f1308684890a2f517370ff55f\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":4096,\"p\":6,\"r\":8,\"salt\":\"564d5238b38a9f5eceb36e0d9d8e52fff79cebefea61d4a597d2390784763362\"},\"mac\":\"e2f685bd297d41abab6e532401a17d1182d0e055af96991757751cbe1c032e36\"}}";

    public static final String TO_ADDRESS = "0x88189a9d3b0ccbc370e67b91211abf7f33fcf6d5";
}
