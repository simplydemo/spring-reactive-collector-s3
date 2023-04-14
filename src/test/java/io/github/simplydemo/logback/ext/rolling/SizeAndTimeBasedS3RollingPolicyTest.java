package io.github.simplydemo.logback.ext.rolling;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SizeAndTimeBasedS3RollingPolicyTest {


    @Test
    public void test(){
        SizeAndTimeBasedS3RollingPolicy policy = new SizeAndTimeBasedS3RollingPolicy();
        policy.setBucket("");
        policy.setRegion("");
        policy.setProfile("");
        Assertions.assertNotNull(policy);
    }
}
