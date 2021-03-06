//package in.technozion.technozion.Data;
//
///**
// * Created by karnika_ag on 9/26/2015.
// */
//// package org.apache.http.util;
//
//     //   import java.io.UnsupportedEncodingException;
//
////        import org.apache.http.protocol.HTTP;
//
///**
// * The home for utility methods that handle various encoding tasks.
// *
// * @author Michael Becke
// * @author <a href="mailto:oleg at ural.ru">Oleg Kalnichevski</a>
// *
// * @since 4.0
// *
// * @deprecated Please use {@link java.net.URL#openConnection} instead.
// *     Please visit <a href="http://android-developers.blogspot.com/2011/09/androids-http-clients.html">this webpage</a>
// *     for further details.
// */
////@Deprecated
////public final class EncodingUtils {
//
//    /**
//     * Converts the byte array of HTTP content characters to a string. If
//     * the specified charset is not supported, default system encoding
//     * is used.
//     *
//     * @param data the byte array to be encoded
//     * @param offset the index of the first byte to encode
//     * @param length the number of bytes to encode
//     * @param charset the desired character encoding
//     * @return The result of the conversion.
//     */
//    public static String getString(
//            final byte[] data,
//            int offset,
//            int length,
//            String charset
//    ) {
//
//        if (data == null) {
//            throw new IllegalArgumentException("Parameter may not be null");
//        }
//
//        if (charset == null || charset.length() == 0) {
//            throw new IllegalArgumentException("charset may not be null or empty");
//        }
//
//        try {
//            return new String(data, offset, length, charset);
//        } catch (UnsupportedEncodingException e) {
//            return new String(data, offset, length);
//        }
//    }
//
//
//    /**
//     * Converts the byte array of HTTP content characters to a string. If
//     * the specified charset is not supported, default system encoding
//     * is used.
//     *
//     * @param data the byte array to be encoded
//     * @param charset the desired character encoding
//     * @return The result of the conversion.
//     */
//    public static String getString(final byte[] data, final String charset) {
//        if (data == null) {
//            throw new IllegalArgumentException("Parameter may not be null");
//        }
//        return getString(data, 0, data.length, charset);
//    }
//
//    /**
//     * Converts the specified string to a byte array.  If the charset is not supported the
//     * default system charset is used.
//     *
//     * @param data the string to be encoded
//     * @param charset the desired character encoding
//     * @return The resulting byte array.
//     */
//    public static byte[] getBytes(final String data, final String charset) {
//
//        if (data == null) {
//            throw new IllegalArgumentException("data may not be null");
//        }
//
//        if (charset == null || charset.length() == 0) {
//            throw new IllegalArgumentException("charset may not be null or empty");
//        }
//
//        try {
//            return data.getBytes(charset);
//        } catch (UnsupportedEncodingException e) {
//            return data.getBytes();
//        }
//    }
//
//    /**
//     * Converts the specified string to byte array of ASCII characters.
//     *
//     * @param data the string to be encoded
//     * @return The string as a byte array.
//     */
//    public static byte[] getAsciiBytes(final String data) {
//
//        if (data == null) {
//            throw new IllegalArgumentException("Parameter may not be null");
//        }
//
//        try {
//            return data.getBytes(HTTP.US_ASCII);
//        } catch (UnsupportedEncodingException e) {
//            throw new Error("HttpClient requires ASCII support");
//        }
//    }
//
//    /**
//     * Converts the byte array of ASCII characters to a string. This method is
//     * to be used when decoding content of HTTP elements (such as response
//     * headers)
//     *
//     * @param data the byte array to be encoded
//     * @param offset the index of the first byte to encode
//     * @param length the number of bytes to encode
//     * @return The string representation of the byte array
//     */
//    public static String getAsciiString(final byte[] data, int offset, int length) {
//
//        if (data == null) {
//            throw new IllegalArgumentException("Parameter may not be null");
//        }
//
//        try {
//            return new String(data, offset, length, HTTP.US_ASCII);
//        } catch (UnsupportedEncodingException e) {
//            throw new Error("HttpClient requires ASCII support");
//        }
//    }
//
//    /**
//     * Converts the byte array of ASCII characters to a string. This method is
//     * to be used when decoding content of HTTP elements (such as response
//     * headers)
//     *
//     * @param data the byte array to be encoded
//     * @return The string representation of the byte array
//     */
//    public static String getAsciiString(final byte[] data) {
//        if (data == null) {
//            throw new IllegalArgumentException("Parameter may not be null");
//        }
//        return getAsciiString(data, 0, data.length);
//    }
//
//    /**
//     * This class should not be instantiated.
//     */
//    private EncodingUtils() {
//    }
//
//}