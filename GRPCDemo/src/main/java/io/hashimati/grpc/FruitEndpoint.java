package io.hashimati.grpc;

import io.grpc.stub.StreamObserver;
import io.hashimati.*;
import io.hashimati.domains.Fruit;
import io.hashimati.services.FruitService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class FruitEndpoint extends
        FruitGrpcServiceGrpc.FruitGrpcServiceImplBase {

    @Inject
    private FruitService fruitService;

    @Override
    public void save(FruitGrpc request, StreamObserver<MessageReply> responseObserver) {
        super.save(request, responseObserver);
        Fruit fruit = new Fruit(){{
            setName(request.getName());
            setPrice(request.getPrice());
            setQuantity(request.getQuantity());
        }};
        Fruit result = fruitService.save(fruit);
        if(result != null)
            responseObserver.onNext(MessageReply.newBuilder().setMessage("Done").build());
        else
            responseObserver.onNext(MessageReply.newBuilder().setMessage("Failed").build());
        responseObserver.onCompleted();;

    }

    @Override
    public void update(FruitGrpc request, StreamObserver<MessageReply> responseObserver) {
        super.update(request, responseObserver);
        Fruit fruit = new Fruit(){{
            setName(request.getName());
            setPrice(request.getPrice());
            setQuantity(request.getQuantity());
        }};
        Fruit result = fruitService.update(fruit);
        if(result != null)
            responseObserver.onNext(MessageReply.newBuilder().setMessage("Done").build());
        else
            responseObserver.onNext(MessageReply.newBuilder().setMessage("Failed").build());
        responseObserver.onCompleted();
    }

    @Override
    public void delete(IdQuery request, StreamObserver<MessageReply> responseObserver) {
        super.delete(request, responseObserver);
        responseObserver.onNext(
                fruitService.deleteById(request.getId())?
                        MessageReply.newBuilder().setMessage("Done").build():
                        MessageReply.newBuilder().setMessage("Failed").build());

        responseObserver.onCompleted();
    }

    @Override
    public void findById(IdQuery request, StreamObserver<FruitGrpc> responseObserver) {
        super.findById(request, responseObserver);

        Fruit fruit = fruitService.findById(request.getId());
        responseObserver.onNext(FruitGrpc
                .newBuilder()
                .setName(fruit.getName())
                .setPrice(fruit.getPrice())
                .setQuantity(fruit.getQuantity())
                .setId(fruit.getId())
                .build());
    }
    @Override
    public void findAll(PageQuery request, StreamObserver<FruitGrpc> responseObserver) {
        super.findAll(request, responseObserver);
        fruitService.findAll().forEach(
                x-> responseObserver.onNext(FruitGrpc.newBuilder()
                        .setName(x.getName())
                        .setId(x.getId())
                        .setQuantity(x.getQuantity())
                        .setPrice(x.getPrice())
                        .build())
        );
        responseObserver.onCompleted();
    }
}
