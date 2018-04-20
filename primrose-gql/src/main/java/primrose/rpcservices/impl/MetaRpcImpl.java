package primrose.rpcservices.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;

import primrose.data.MetaRepository;
import primrose.data.impl.MetaRepositoryImpl.MetaTypes;
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
      return metaRepository.list(MetaTypes.CUSTOMER);
   }

   @Override
   public List<MetaType> customerRelation() {
      return metaRepository.list(MetaTypes.CUSTOMER_RELATION);
   }

   @Override
   public List<MetaType> address() {
      return metaRepository.list(MetaTypes.ADDRESS);
   }

   @Override
   public List<MetaType> phoneNumber() {
      return metaRepository.list(MetaTypes.PHONE_NUMBER);
   }

   @Override
   public List<MetaType> email() {
      return metaRepository.list(MetaTypes.EMAIL);
   }

   @Override
   public List<MetaType> contact() {
      return metaRepository.list(MetaTypes.CONTACT);
   }

   @Override
   public List<MetaType> meeting() {
      return metaRepository.list(MetaTypes.MEETING);
   }

}
