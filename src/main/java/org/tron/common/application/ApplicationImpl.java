package org.tron.common.application;

import lombok.extern.slf4j.Slf4j;

import org.tron.core.db.BlockStore;
import org.tron.core.db.Manager;
import org.tron.core.net.node.Node;
import org.tron.core.net.node.NodeDelegate;
import org.tron.core.net.node.NodeDelegateImpl;
import org.tron.core.net.node.NodeImpl;
import org.tron.core.config.args.Args;

@Slf4j
public class ApplicationImpl implements Application {
  private Node p2pNode;
  private BlockStore blockStoreDb;
  private ServiceContainer services;
  private NodeDelegate nodeDelegate;

  private Manager dbManager;

  private boolean isProducer;

  private void resetP2PNode() {
    p2pNode.listen();
    p2pNode.connectToP2PNetWork();
    p2pNode.syncFrom(blockStoreDb.getHeadBlockId());
  }
  
  @Override
  public void setOptions(Args args) {

  }

  @Override
  public void init(String path, Args args) {
    p2pNode = new NodeImpl();
    dbManager = new Manager();
    dbManager.init();
    blockStoreDb = dbManager.getBlockStore();
    services = new ServiceContainer();
    nodeDelegate = new NodeDelegateImpl(dbManager);
  }

  @Override
  public void addService(Service service) {
    services.add(service);
  }

  @Override
  public void initServices(Args args) {
    services.init(args);
  }

  /**
   * start up the app.
   */
  public void startup() {
    p2pNode.setNodeDelegate(nodeDelegate);
    resetP2PNode();
  }

  @Override
  public void shutdown() {

  }

  @Override
  public void startServices() {
    services.start();
  }

  @Override
  public void shutdownServices() {
    services.stop();
  }

  @Override
  public Node getP2pNode() {
    return p2pNode;
  }

  @Override
  public BlockStore getBlockStoreS() {
    return blockStoreDb;
  }

  @Override
  public Manager getDbManager() {
    return dbManager;
  }

  public boolean isProducer() {
    return isProducer;
  }

  public void setIsProducer(boolean producer) {
    isProducer = producer;
  }

}
