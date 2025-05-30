package com.example.aivle.global.exception;

public class InvalidApiKeyException extends CoverGenerationException {
    public InvalidApiKeyException() { super("Invalid or missing OpenAI API key"); }
}
