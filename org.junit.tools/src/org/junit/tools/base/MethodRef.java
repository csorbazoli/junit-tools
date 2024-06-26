package org.junit.tools.base;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

/**
 * Class implementation for the method-reference annotation.
 * 
 * @author JUnit-Tools-Team
 * 
 */
public class MethodRef {

    private String name;

    private String signature;

    private boolean signatureChanged = false;

    private String signatureNew;

    private boolean unresolvedConflict = false;

    public MethodRef(String name, String signature) {
	this.name = name;
	this.signature = signature;
    }

    public String getName() {
	return name;
    }

    public String getSignature() {
	return signature;
    }

    @Override
    public String toString() {
	return name + signature;
    }

    public boolean isSignatureChanged() {
	return signatureChanged;
    }

    public void setSignatureChanged(boolean signatureChanged) {
	this.signatureChanged = signatureChanged;
    }

    public void setSignatureNew(String signatureNew) {
	setSignatureChanged(true);
	this.signatureNew = signatureNew;
    }

    public String getSignatureNew() {
	return signatureNew;
    }

    public void setUnresolvedConflict(boolean unresolvedConflict) {
	this.unresolvedConflict = unresolvedConflict;
    }

    public boolean isUnresolvedConflict() {
	return unresolvedConflict;
    }

    public String getSignatureToCompare() {
	if (isSignatureChanged()) {
	    return getSignatureNew();
	}
	return getSignature();
    }

    public boolean isEquals(MethodRef mrToCompare) {
	if (mrToCompare.getName().equals(getName())
		&& mrToCompare.getSignatureToCompare().equals(
			getSignatureToCompare())) {
	    return true;
	}

	return false;
    }

    public boolean isEquals(IMethod methodToCompare) {
	try {
	    return isEquals(new MethodRef(methodToCompare.getElementName(),
		    methodToCompare.getSignature()));
	} catch (JavaModelException e) {
	    return false;
	}
    }

    @Override
    public int hashCode() {
	int PRIME = 31;
	int ret = 1;
	ret = ret * PRIME + super.hashCode();
	ret = ret * PRIME + (this.name == null ? 0 : this.name.hashCode());
	ret = ret * PRIME + (this.signature == null ? 0 : this.signature.hashCode());
	return ret;
    }

}
