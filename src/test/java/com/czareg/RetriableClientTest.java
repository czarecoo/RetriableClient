package com.czareg;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class RetriableClientTest {
    public static final String TEST_HOSTNAME = "testHost";

    @Test
    public void shouldReturnExpectedValue() throws ClientException {
        Client client = mock(Client.class);
        String expected = TEST_HOSTNAME;
        when(client.getHostname()).thenReturn(expected);
        RetriableClient retriableClient = new RetriableClient(client);

        String result = retriableClient.getHostname();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void shouldCallUnderlyingClientOnce() throws ClientException {
        Client client = mock(Client.class);
        when(client.getHostname()).thenReturn(TEST_HOSTNAME);
        RetriableClient retriableClient = new RetriableClient(client);

        retriableClient.getHostname();

        verify(client, Mockito.times(1)).getHostname();
    }

    @Test(expected = ClientException.class)
    public void shouldThrowExpectedException() throws ClientException {
        Client client = mock(Client.class);
        when(client.getHostname()).thenThrow(ClientException.class);
        RetriableClient retriableClient = new RetriableClient(client);

        retriableClient.getHostname();
    }

    @Test(expected = ClientException.class)
    public void shouldTryCallingMethodTwiceOnExpectedException() throws ClientException {
        Client client = mock(Client.class);
        when(client.getHostname()).thenThrow(ClientException.class);
        RetriableClient retriableClient = new RetriableClient(client);

        retriableClient.getHostname();

        verify(client, Mockito.times(2)).getHostname();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowUnexpectedException() throws ClientException {
        Client client = mock(Client.class);
        when(client.getHostname()).thenThrow(IllegalStateException.class);
        RetriableClient retriableClient = new RetriableClient(client);

        retriableClient.getHostname();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldTryCallingMethodOnceOnUnexpectedException() throws ClientException {
        Client client = mock(Client.class);
        when(client.getHostname()).thenThrow(IllegalStateException.class);
        RetriableClient retriableClient = new RetriableClient(client);

        retriableClient.getHostname();

        verify(client, Mockito.times(1)).getHostname();
    }

    @Test
    public void shouldReturnExpectedValueWhenSecondTrySucceeds() throws ClientException {
        Client client = mock(Client.class);
        String expected = TEST_HOSTNAME;
        when(client.getHostname())
                .thenThrow(ClientException.class)
                .thenReturn(expected);
        RetriableClient retriableClient = new RetriableClient(client);

        String result = retriableClient.getHostname();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void shouldTryCallingMethodTwiceWhenFirstTryThrewExpectedException() throws ClientException {
        Client client = mock(Client.class);
        when(client.getHostname())
                .thenThrow(ClientException.class)
                .thenReturn(TEST_HOSTNAME);
        RetriableClient retriableClient = new RetriableClient(client);

        retriableClient.getHostname();

        verify(client, Mockito.times(2)).getHostname();
    }
}
