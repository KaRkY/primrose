package primrose.rpcservices;

import java.util.List;

import com.googlecode.jsonrpc4j.JsonRpcService;

import primrose.service.MetaType;

@JsonRpcService("/meta")
public interface MetaRpc {

  List<MetaType> customer();

  List<MetaType> customerRelation();

  List<MetaType> address();

  List<MetaType> phoneNumber();

  List<MetaType> email();

  List<MetaType> contact();

  List<MetaType> meeting();
}
