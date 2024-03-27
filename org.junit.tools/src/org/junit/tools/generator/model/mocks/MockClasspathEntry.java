package org.junit.tools.generator.model.mocks;

import static org.junit.tools.generator.model.mocks.MockConstants.NOT_IMPLEMENTED;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MockClasspathEntry implements IClasspathEntry {

    private IAccessRule[] accessRules;
    private int contentKind;
    private int entryKind;
    private IPath[] exclusionPatterns;
    private IClasspathAttribute[] extraAttributes;
    private IPath[] inclusionPatterns;
    private IPath outputLocation;
    private IPath path;
    private IClasspathEntry referencingEntry;
    private IClasspathEntry resolvedEntry;
    private IPath sourceAttachmentPath;
    private IPath sourceAttachmentRootPath;
    private boolean exported;

    @Override
    public boolean combineAccessRules() {
	return false;
    }

    @Override
    public IPath getExternalAnnotationPath(IProject arg0, boolean arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

}
