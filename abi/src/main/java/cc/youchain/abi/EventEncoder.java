package cc.youchain.abi;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import cc.youchain.abi.datatypes.Address;
import cc.youchain.abi.datatypes.Event;
import cc.youchain.abi.datatypes.Type;
import cc.youchain.abi.datatypes.generated.Uint256;
import cc.youchain.crypto.Hash;
import cc.youchain.utils.Numeric;

/**
 * <p>YOUChain filter encoding.
 * Further limited details are available.
 * </p>
 */
public class EventEncoder {

    private EventEncoder() { }

    public static String encode(Event event) {

        String methodSignature = buildMethodSignature(
                event.getName(),
                event.getParameters());

        return buildEventSignature(methodSignature);
    }

    static <T extends Type> String buildMethodSignature(
            String methodName, List<TypeReference<T>> parameters) {

        StringBuilder result = new StringBuilder();
        result.append(methodName);
        result.append("(");
        String params = parameters.stream()
                .map(p -> Utils.getTypeName(p))
                .collect(Collectors.joining(","));
        result.append(params);
        result.append(")");
        return result.toString();
    }

    public static String buildEventSignature(String methodSignature) {
        byte[] input = methodSignature.getBytes();
        byte[] hash = Hash.sha3(input);
        return Numeric.toHexString(hash);
    }
    
    public static void main(String[] args) {
        String methodSignature = "Burn(address,uint256)";
        String s2 = buildEventSignature(methodSignature);


        System.out.println(s2);
//
//        Event event = new Event("Transfer", Arrays.asList(
//                new TypeReference<Address>() {
//                },
//                new TypeReference<Address>() {
//                },
//                new TypeReference<Uint256>() {
//                }));
//        String encodedEventSignature = EventEncoder.encode(event);
//        System.out.println(encodedEventSignature);

        String methodSignature1 = "approve(address,uint256)";
        String s3 = buildEventSignature(methodSignature1);
        System.out.println(s3);
    }
}
