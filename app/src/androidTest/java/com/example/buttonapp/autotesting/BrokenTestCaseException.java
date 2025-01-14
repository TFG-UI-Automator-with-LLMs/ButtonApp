package com.example.buttonapp.autotesting;

import androidx.test.uiautomator.UiObjectNotFoundException;

public class BrokenTestCaseException extends  RuntimeException {

    private TestCase brokenTestCase;

    private long breakingIndex;

    public BrokenTestCaseException(String msg, TestCase brokenTestCase, long breakingIndex) {
        super(msg);
        this.brokenTestCase=brokenTestCase;
        this.breakingIndex=breakingIndex;
    }

    public TestCase getBrokenTestCase() {
        return brokenTestCase;
    }

    public long getBreakingIndex() {
        return breakingIndex;
    }
}