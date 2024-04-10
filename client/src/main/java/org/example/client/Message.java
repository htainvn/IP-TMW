package org.example.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    protected String    messageHeader;
    protected String    toHost;
    protected Integer   toPort;
}
