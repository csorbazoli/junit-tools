package org.junit.tools.generator.model.mocks;

import static org.junit.tools.generator.model.mocks.MockConstants.NOT_IMPLEMENTED;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IModularClassFile;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.IOrdinaryClassFile;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.WorkingCopyOwner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MockPackageFragment implements IPackageFragment {

    private MockCompilationUnit parent;

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

    private int elementType;

    private String handleIdentifier;

    private IJavaModel javaModel;

    private IJavaProject javaProject;

    private IOpenable openable;

    private IPath path;

    private IJavaElement primaryElement;

    private IResource resource;

    private ISchedulingRule schedulingRule;

    @Override
    public IResource getUnderlyingResource() throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    private boolean isReadOnly;

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

    private boolean open;

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
    public void copy(IJavaElement arg0, IJavaElement arg1, String arg2, boolean arg3, IProgressMonitor arg4) throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void delete(boolean arg0, IProgressMonitor arg1) throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void move(IJavaElement arg0, IJavaElement arg1, String arg2, boolean arg3, IProgressMonitor arg4) throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void rename(String arg0, boolean arg1, IProgressMonitor arg2) throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean containsJavaResources() throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ICompilationUnit createCompilationUnit(String fileName, String arg1, boolean arg2, IProgressMonitor arg3) throws JavaModelException {
	return MockCompilationUnit.builder()
		.elementName(fileName)
		.build();
    }

    @Override
    public IClassFile[] getAllClassFiles() throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IClassFile getClassFile(String arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IClassFile[] getClassFiles() throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ICompilationUnit getCompilationUnit(String arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ICompilationUnit[] getCompilationUnits() throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ICompilationUnit[] getCompilationUnits(WorkingCopyOwner arg0) throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    private String elementName;

    @Override
    public int getKind() throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    private IModularClassFile modularClassFile;

    @Override
    public Object[] getNonJavaResources() throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IOrdinaryClassFile getOrdinaryClassFile(String arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IOrdinaryClassFile[] getOrdinaryClassFiles() throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean hasSubpackages() throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    private boolean defaultPackage;

}
