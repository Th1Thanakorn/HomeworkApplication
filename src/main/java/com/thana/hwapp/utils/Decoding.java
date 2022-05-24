package com.thana.hwapp.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Decoding {

    public static final String STUDENT_KEY = "NDQ5NTUsNDQ5NjAsNDQ5NjMsNDQ5ODQsNDQ5ODksNDQ5OTQsNDUwMDIsNDUwMzQsNDUwNDAsNDUwNDMsNDUwNTEsNDUwNzEsNDUwNzIsNDUwOTMsNDUxMDgsNDUxMTAsNDUxMTQsNDUxMTcsNDUxMzMsNDUxNjQsNDUxNzMsNDUyMTIsNDUyMjAsNDUyNDcsNDUyNzMsNDUzMDAsNDUzMzgsNDUzNDgsNDUzNTYsNDUzNTcsNDUzNzQsNDUzODgsNDU0MzksNDU0NjMsNDU0NzM";

    public static String decodeStudentId() {
        return new String(Base64.getDecoder().decode(STUDENT_KEY), StandardCharsets.UTF_8);
    }
}
