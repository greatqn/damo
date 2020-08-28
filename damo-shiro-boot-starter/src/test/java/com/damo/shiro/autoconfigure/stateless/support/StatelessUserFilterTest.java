package com.damo.shiro.autoconfigure.stateless.support;

import org.junit.Test;

import com.damo.shiro.autoconfigure.stateless.support.StatelessUserFilter;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * 
 * 
 */
public class StatelessUserFilterTest {
    @Test
    public void acceptHtml() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getHeader("Accept")).thenReturn("text/html");
        assertThat(StatelessUserFilter.acceptHtml(req)).isTrue();

        when(req.getHeader("Accept")).thenReturn("application/json");
        assertThat(StatelessUserFilter.acceptHtml(req)).isFalse();

        when(req.getHeader("Accept")).thenReturn("");
        assertThat(StatelessUserFilter.acceptHtml(req)).isFalse();

        when(req.getHeader("Accept")).thenReturn(null);
        assertThat(StatelessUserFilter.acceptHtml(req)).isFalse();
    }
}