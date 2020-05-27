package netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.bean.module.testModule.request.OperationOneRequest;
import netty.bean.module.testModule.response.OperationTwoResponse;
import netty.coder.MyRequest;
import netty.coder.MyResponse;
import netty.coder.StateEnum;
import netty.util.InvokeUtil;
import serializer.NettySerializer;

/**
 * @author linhao
 * @date 2020/5/16 16:29
 */
public class RequestHandler extends SimpleChannelInboundHandler<MyRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MyRequest myRequest) throws Exception {
        MyResponse response = new MyResponse(myRequest);
        try {
            short mid = response.getModule();
            short oid = response.getOperation();
            byte[] reqData = myRequest.getData();
            Object result = InvokeUtil.invoke(mid, oid, reqData);

            OperationTwoResponse operationTwoResponse = new OperationTwoResponse();
            operationTwoResponse.setId(2);
            operationTwoResponse.setMoney(55.55);

            InvokeUtil.createResp(response, operationTwoResponse);
            response.setState(StateEnum.SUCCESS);
//            if (myRequest.getModule() == 1){
//                if (myRequest.getOperation() == 1){
//                    // 反序列化请求实体
//                    NettySerializer nettySerializer = new NettySerializer();
//                    byte[] operationData = myRequest.getData();
//                    OperationOneRequest operationOneRequest = nettySerializer.deserialize(operationData, OperationOneRequest.class);
//                    System.out.println("id:"+operationOneRequest.getId());
//                    System.out.println("name:"+operationOneRequest.getName());
//
//                    // 序列化响应实体
//                    OperationTwoResponse operationTwoResponse = new OperationTwoResponse();
//                    operationTwoResponse.setId(2);
//                    operationTwoResponse.setMoney(55.55);
//                    byte[] resp = nettySerializer.serialize(operationTwoResponse);
//
//                    // 响应
//                    response.setData(resp);
//                    response.setState(StateEnum.SUCCESS);
//
//                    channelHandlerContext.channel().writeAndFlush(response);
//                }else if (myRequest.getOperation() == 2){
//
//                }
//            }
        }catch (Exception e){
            response.setState(StateEnum.FAIL);
        }finally {
            channelHandlerContext.channel().writeAndFlush(response);
        }
    }
}
