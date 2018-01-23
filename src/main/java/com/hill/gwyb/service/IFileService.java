package com.hill.gwyb.service;

import java.util.List;
import java.util.Map;

public interface IFileService {
    Map create(List<String> headList, List<String[]> bodyList) throws Exception;

    Map getData(List<String> head, List<String> body) throws Exception;
}
