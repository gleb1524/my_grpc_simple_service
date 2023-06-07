package org.example;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import ru.karaban.grpc.SimpleImageServiceGrpc;
import ru.karaban.grpc.TelemetryProto;
import ru.karaban.grpc.TelemetryProto.ImageResponse;


public class ImageService extends SimpleImageServiceGrpc.SimpleImageServiceImplBase {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private boolean isStream = true;

    @Override
    public void getImage(TelemetryProto.ImageRequest request,
                         StreamObserver<ImageResponse> responseObserver) {
        captureCameraById(0, responseObserver);
        responseObserver.onCompleted();
    }

    private void captureCameraById(int cameraId, StreamObserver<ImageResponse> responseObserver) {

        VideoCapture capture = new VideoCapture(cameraId);
        Mat frame = new Mat();

        while (isStream) {
            capture.read(frame);
            ByteString data = ByteString.copyFrom(encoding(frame));
            sendStreamImg(responseObserver, data);
        }

        capture.release();
    }

    private void sendStreamImg(StreamObserver<ImageResponse> responseObserver, ByteString data) {
        ImageResponse response = ImageResponse.newBuilder()
                .setData(data)
                .build();
        responseObserver.onNext(response);
    }

    private byte[] encoding(Mat frame) {
        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".jpg", frame, buffer, new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, 80));
        return buffer.toArray();
    }
}
