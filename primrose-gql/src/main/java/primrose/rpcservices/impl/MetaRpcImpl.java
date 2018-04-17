package primrose.rpcservices.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;

import primrose.data.MetaRepository;
import primrose.rpcservices.MetaRpc;
import primrose.service.MetaType;

@AutoJsonRpcServiceImpl
@Component
public class MetaRpcImpl implements MetaRpc {

  private MetaRepository metaRepository;

  public MetaRpcImpl(MetaRepository metaRepository) {
    this.metaRepository = metaRepository;
  }

  @Override
  public List<MetaType> customer() {
    return metaRepository.customer();
  }

  @Override
  public List<MetaType> customerRelation() {
    return metaRepository.customerRelation();
  }

  @Override
  public List<MetaType> address() {
    return metaRepository.address();
  }

  @Override
  public List<MetaType> phoneNumber() {
    return metaRepository.phoneNumber();
  }

  @Override
  public List<MetaType> email() {
    return metaRepository.email();
  }

  @Override
  public List<MetaType> contact() {
    return metaRepository.contact();
  }

  @Override
  public List<MetaType> meeting() {
    return metaRepository.meeting();
  }

}
