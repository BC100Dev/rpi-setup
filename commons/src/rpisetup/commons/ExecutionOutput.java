package rpisetup.commons;

public record ExecutionOutput(int rc, byte[] outputStreamData, byte[] errorStreamData) {
}
