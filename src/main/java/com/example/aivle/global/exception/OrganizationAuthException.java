package com.example.aivle.global.exception;

public class OrganizationAuthException extends CoverGenerationException {
    public OrganizationAuthException() { super("Access denied: check organization or permission"); }
}
