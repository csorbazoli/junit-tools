package org.junit.tools.generator.model.mocks;

import static org.junit.tools.generator.model.mocks.MockConstants.NOT_IMPLEMENTED;

import java.io.File;

import org.eclipse.core.runtime.IPath;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIncludeProperties({ "elementName" })
public class MockPath implements IPath {

    private String elementName;

    @Override
    public MockPath clone() {
	return MockPath.builder()
		.build();
    }

    @Override
    public IPath addFileExtension(String arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPath addTrailingSeparator() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPath append(String arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPath append(IPath arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String getDevice() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String getFileExtension() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean hasTrailingSeparator() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isAbsolute() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isEmpty() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isPrefixOf(IPath arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isRoot() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isUNC() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isValidPath(String arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isValidSegment(String arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String lastSegment() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPath makeAbsolute() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPath makeRelative() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPath makeRelativeTo(IPath arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPath makeUNC(boolean arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public int matchingFirstSegments(IPath arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPath removeFileExtension() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPath removeFirstSegments(int arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPath removeLastSegments(int arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPath removeTrailingSeparator() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String segment(int arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public int segmentCount() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String[] segments() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPath setDevice(String arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public File toFile() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String toOSString() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String toPortableString() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPath uptoSegment(int arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

}
