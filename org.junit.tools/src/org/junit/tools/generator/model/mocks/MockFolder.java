package org.junit.tools.generator.model.mocks;

import static org.junit.tools.generator.model.mocks.MockConstants.NOT_IMPLEMENTED;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.eclipse.core.resources.FileInfoMatcherDescription;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IPathVariableManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceFilterDescription;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.jobs.ISchedulingRule;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIncludeProperties({ "fullPath", "location" })
public class MockFolder implements IFolder {

    private IPath fullPath;
    private IPath location;

    @Override
    public IResourceFilterDescription createFilter(int arg0, FileInfoMatcherDescription arg1, int arg2, IProgressMonitor arg3) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean exists(IPath arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IFile[] findDeletedMembersWithHistory(int arg0, IProgressMonitor arg1) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IResource findMember(String arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IResource findMember(IPath arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IResource findMember(String arg0, boolean arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IResource findMember(IPath arg0, boolean arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String getDefaultCharset() throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String getDefaultCharset(boolean arg0) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IFile getFile(IPath arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IResourceFilterDescription[] getFilters() throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IFolder getFolder(IPath arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IResource[] members() throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IResource[] members(boolean arg0) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IResource[] members(int arg0) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void setDefaultCharset(String arg0) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void setDefaultCharset(String arg0, IProgressMonitor arg1) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void accept(IResourceVisitor arg0) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void accept(IResourceProxyVisitor arg0, int arg1) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void accept(IResourceProxyVisitor arg0, int arg1, int arg2) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void accept(IResourceVisitor arg0, int arg1, boolean arg2) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void accept(IResourceVisitor arg0, int arg1, int arg2) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void clearHistory(IProgressMonitor arg0) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void copy(IPath arg0, boolean arg1, IProgressMonitor arg2) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void copy(IPath arg0, int arg1, IProgressMonitor arg2) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void copy(IProjectDescription arg0, boolean arg1, IProgressMonitor arg2) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void copy(IProjectDescription arg0, int arg1, IProgressMonitor arg2) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IMarker createMarker(String arg0) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IResourceProxy createProxy() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void delete(boolean arg0, IProgressMonitor arg1) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void delete(int arg0, IProgressMonitor arg1) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void deleteMarkers(String arg0, boolean arg1, int arg2) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean exists() {
	return true;
    }

    @Override
    public IMarker findMarker(long arg0) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IMarker[] findMarkers(String arg0, boolean arg1, int arg2) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public int findMaxProblemSeverity(String arg0, boolean arg1, int arg2) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String getFileExtension() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public long getLocalTimeStamp() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public URI getLocationURI() {
	try {
	    return new URI(location.toOSString());
	} catch (URISyntaxException e) {
	    throw new IllegalArgumentException(location.toOSString(), e);
	}
    }

    @Override
    public IMarker getMarker(long arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public long getModificationStamp() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String getName() {
	return location.lastSegment();
    }

    @Override
    public IContainer getParent() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPathVariableManager getPathVariableManager() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public Map<QualifiedName, String> getPersistentProperties() throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String getPersistentProperty(QualifiedName arg0) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IProject getProject() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPath getProjectRelativePath() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPath getRawLocation() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public URI getRawLocationURI() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ResourceAttributes getResourceAttributes() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public Map<QualifiedName, Object> getSessionProperties() throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public Object getSessionProperty(QualifiedName arg0) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public int getType() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IWorkspace getWorkspace() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isAccessible() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isDerived() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isDerived(int arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isHidden() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isHidden(int arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isLinked() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isLinked(int arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isLocal(int arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isPhantom() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isReadOnly() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isSynchronized(int arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isTeamPrivateMember() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isTeamPrivateMember(int arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isVirtual() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void move(IPath arg0, boolean arg1, IProgressMonitor arg2) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void move(IPath arg0, int arg1, IProgressMonitor arg2) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void move(IProjectDescription arg0, int arg1, IProgressMonitor arg2) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void move(IProjectDescription arg0, boolean arg1, boolean arg2, IProgressMonitor arg3) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void refreshLocal(int arg0, IProgressMonitor arg1) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void revertModificationStamp(long arg0) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void setDerived(boolean arg0) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void setDerived(boolean arg0, IProgressMonitor arg1) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void setHidden(boolean arg0) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void setLocal(boolean arg0, int arg1, IProgressMonitor arg2) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public long setLocalTimeStamp(long arg0) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void setPersistentProperty(QualifiedName arg0, String arg1) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void setReadOnly(boolean arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void setResourceAttributes(ResourceAttributes arg0) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void setSessionProperty(QualifiedName arg0, Object arg1) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void setTeamPrivateMember(boolean arg0) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void touch(IProgressMonitor arg0) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public <T> T getAdapter(Class<T> arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean contains(ISchedulingRule arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isConflicting(ISchedulingRule arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void create(boolean arg0, boolean arg1, IProgressMonitor arg2) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void create(int arg0, boolean arg1, IProgressMonitor arg2) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void createLink(IPath arg0, int arg1, IProgressMonitor arg2) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void createLink(URI arg0, int arg1, IProgressMonitor arg2) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void delete(boolean arg0, boolean arg1, IProgressMonitor arg2) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IFile getFile(String arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IFolder getFolder(String arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void move(IPath arg0, boolean arg1, boolean arg2, IProgressMonitor arg3) throws CoreException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

}
