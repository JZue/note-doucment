package com.jzue.study.duolu;

/**
 * @author jzue
 * @date 2021/3/8 2:49 下午
 **/
public class Proactor {
//    class RedisClient Implements ChannelHandler{
//        private BlockingQueue CmdQueue;
//        private EventLoop eventLoop;
//        private Channel channel;
//        class Cmd{
//            String cmd;
//            Future result;
//        }
//        public Future get(String key){
//            Cmd cmd= new Cmd(key);
//            queue.offer(cmd);
//            eventLoop.submit(new Runnable(){
//                List list = new ArrayList();
//        queue.drainTo(list);
//        if(channel.isWritable()){
//                    channel.writeAndFlush(list);
//                }
//            });
//        }
//        public void ChannelReadFinish(Channel channel，Buffer Buffer){
//            List result = handleBuffer();//处理数据
//            //从cmdQueue取出future，并设值，future.done();
//        }
//        public void ChannelWritable(Channel channel){
//            channel.flush();
//        }
//    }
}
