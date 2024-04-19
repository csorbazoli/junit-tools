package org.junit.tools.generator.model.mocks;

import static org.junit.tools.generator.model.mocks.MockConstants.NOT_IMPLEMENTED;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIncludeProperties({ "contentKind", "entryKind", "exported", "path" })
public class MockClasspathEntry implements IClasspathEntry {

    private int contentKind;
    private int entryKind;
    private boolean exported;
    private MockPath path;

    @Override
    public boolean combineAccessRules() {
	return false;
    }

    @Override
    public IPath getExternalAnnotationPath(IProject arg0, boolean arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IAccessRule[] getAccessRules() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPath[] getExclusionPatterns() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IClasspathAttribute[] getExtraAttributes() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPath[] getInclusionPatterns() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPath getOutputLocation() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public MockPath getPath() {
	return path;
    }

    @Override
    public IClasspathEntry getReferencingEntry() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IClasspathEntry getResolvedEntry() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPath getSourceAttachmentPath() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPath getSourceAttachmentRootPath() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

}
