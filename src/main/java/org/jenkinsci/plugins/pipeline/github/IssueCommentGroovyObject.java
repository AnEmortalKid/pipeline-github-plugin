package org.jenkinsci.plugins.pipeline.github;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import groovy.lang.GroovyObjectSupport;
import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.service.IssueService;
import org.jenkinsci.plugins.scriptsecurity.sandbox.whitelists.Whitelisted;

import java.io.IOException;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.util.Date;
import java.util.Objects;

/**
 * Groovy wrapper over a {@link Comment}
 *
 * Additionally provides one the ability to update the comment body and delete the comment.
 *
 * @author Aaron Whiteside
 * @see Comment
 */
@SuppressFBWarnings("SE_BAD_FIELD")
public class IssueCommentGroovyObject extends GroovyObjectSupport implements Serializable {
    private static final long serialVersionUID = 1L;

    private final RepositoryId base;
    private final IssueService issueService;
    private Comment comment;

    IssueCommentGroovyObject(final Comment comment, final RepositoryId base, final IssueService issueService) {
        Objects.requireNonNull(comment, "comment cannot be null");
        Objects.requireNonNull(base, "base cannot be null");
        Objects.requireNonNull(issueService, "issueService cannot be null");

        this.comment = comment;
        this.base = base;
        this.issueService = issueService;
    }

    @Whitelisted
    public Date getCreatedAt() {
        return comment.getCreatedAt();
    }

    @Whitelisted
    public Date getUpdatedAt() {
        return comment.getUpdatedAt();
    }

    @Whitelisted
    public String getBody() {
        return comment.getBody();
    }

    @Whitelisted
    public String getBodyHtml() {
        return comment.getBodyHtml();
    }

    @Whitelisted
    public String getBodyText() {
        return comment.getBodyText();
    }

    @Whitelisted
    public long getId() {
        return comment.getId();
    }

    @Whitelisted
    public String getUrl() {
        return comment.getUrl();
    }

    @Whitelisted
    public String getUser() {
        return GitHubHelper.userToLogin(comment.getUser());
    }

    @Whitelisted
    public void setBody(final String body) {
        Comment edit = new Comment();
        edit.setId(comment.getId());
        edit.setBody(body);
        try {
            comment = issueService.editComment(base, edit);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Whitelisted
    public void delete() {
        try {
            issueService.deleteComment(base, comment.getId());
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
