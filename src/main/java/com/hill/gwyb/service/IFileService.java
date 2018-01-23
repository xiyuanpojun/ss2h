package com.hill.gwyb.service;

import java.util.List;
import java.util.Map;

public interface IFileService {
    Map create(String fName, List<String> headList, List<String[]> bodyList) throws Exception;

    Map getData() throws Exception;
}
