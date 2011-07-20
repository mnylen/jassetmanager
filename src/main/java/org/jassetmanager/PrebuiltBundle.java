package org.jassetmanager;

public class PrebuiltBundle extends Bundle {
    public PrebuiltBundle(byte[] content) {
        super.update(new EmptyAssets(), content);
    }
}
