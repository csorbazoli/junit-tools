package org.junit.tools.generator.model.mocks;

import static org.junit.tools.generator.model.mocks.MockConstants.NOT_IMPLEMENTED;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IModuleDescription;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MockPackageFragmentRoot implements IPackageFragmentRoot {

    private IJavaProject javaProject;
    private IPath path;

    @Override
    public IJavaElement[] getChildren() throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean hasChildren() throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean exists() {
	return true;
    }

    @Override
    public IJavaElement getAncestor(int arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String getAttachedJavadoc(IProgressMonitor arg0) throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IResource getCorrespondingResource() throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String getElementName() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public int getElementType() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String getHandleIdentifier() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IJavaModel getJavaModel() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IOpenable getOpenable() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IJavaElement getParent() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IJavaElement getPrimaryElement() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IResource getResource() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ISchedulingRule getSchedulingRule() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IResource getUnderlyingResource() throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isReadOnly() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isStructureKnown() throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public <T> T getAdapter(Class<T> arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void close() throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String findRecommendedLineSeparator() throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IBuffer getBuffer() throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean hasUnsavedChanges() throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isConsistent() throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isOpen() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void makeConsistent(IProgressMonitor arg0) throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void open(IProgressMonitor arg0) throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void save(IProgressMonitor arg0, boolean arg1) throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void attachSource(IPath arg0, IPath arg1, IProgressMonitor arg2) throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void copy(IPath arg0, int arg1, int arg2, IClasspathEntry arg3, IProgressMonitor arg4) throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPackageFragment createPackageFragment(String arg0, boolean arg1, IProgressMonitor arg2) throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void delete(int arg0, int arg1, IProgressMonitor arg2) throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public int getKind() throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IModuleDescription getModuleDescription() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public Object[] getNonJavaResources() throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPackageFragment getPackageFragment(String name) {
	return MockPackageFragment.builder()
		.elementName(name)
		.build();
    }

    @Override
    public IClasspathEntry getRawClasspathEntry() throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IClasspathEntry getResolvedClasspathEntry() throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPath getSourceAttachmentPath() throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPath getSourceAttachmentRootPath() throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isArchive() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isExternal() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void move(IPath arg0, int arg1, int arg2, IClasspathEntry arg3, IProgressMonitor arg4) throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

}
