import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.driver.*;
import jcuda.runtime.JCuda;
import jcuda.runtime.cudaMemcpyKind;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import static jcuda.driver.JCudaDriver.*;

public class TextureFinder extends Thread {


    public void run() {
        long first=System.currentTimeMillis();

        JCudaDriver.setExceptionsEnabled(true);
        cuInit(0);
        CUdevice device = new CUdevice();
        cuDeviceGet(device, 0);
        CUcontext context = new CUcontext();
        cuCtxCreate(context, 0, device);

        CUmodule module = new CUmodule();

        Path tempFile = null;

        try(InputStream in = TextureFinder.class.getResourceAsStream("kernels/texturekernel.ptx")) {
            tempFile = Files.createTempFile("texturekernel", "ptx");
            assert in != null;
            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        cuModuleLoad(module, tempFile.toAbsolutePath().toString());

        CUfunction function = new CUfunction();
        cuModuleGetFunction(function, module, "find");

        int n = Main.formation.size();
        int[] formationRaw = convertFormation(Main.formation);


        CUdeviceptr dFormationRaw = new CUdeviceptr();
        JCuda.cudaMalloc(dFormationRaw, (long) n * 5 * Sizeof.INT);
        JCuda.cudaMemcpy(dFormationRaw, Pointer.to(formationRaw), (long) n * 5 * Sizeof.INT, cudaMemcpyKind.cudaMemcpyHostToDevice);

        Pointer kernelParams = Pointer.to(
                Pointer.to(new int[]{Main.xMin}),
                Pointer.to(new int[]{Main.xMax}),
                Pointer.to(new int[]{Main.yMin}),
                Pointer.to(new int[]{Main.yMax}),
                Pointer.to(new int[]{Main.zMin}),
                Pointer.to(new int[]{Main.zMax}),
                Pointer.to(dFormationRaw),
                Pointer.to(new int[]{n}),
                Pointer.to(new int[]{Main.version.value})
        );

        int width = (Main.xMax-Main.xMin)+1;
        int height = (Main.yMax-Main.yMin)+1;
        int depth = (Main.zMax-Main.zMin)+1;
        long total = (long)width*height*depth;

        int threadsPerBlock = 32;
        int gridX = ((width + threadsPerBlock - 1) / threadsPerBlock);
        int gridZ = ((depth + threadsPerBlock - 1) / threadsPerBlock);


        System.out.println("Starting search with " + ((long)gridX * height * threadsPerBlock*threadsPerBlock*threadsPerBlock) + " threads.");
        cuLaunchKernel(function,
                gridX, height, gridZ,
                threadsPerBlock, 1, threadsPerBlock,
                0, null,
                kernelParams, null
                );

        cuCtxSynchronize();
        cuCtxDestroy(context);

        System.out.println(((System.currentTimeMillis()-first)/1000) + " seconds");
    }

    private int[] convertFormation(ArrayList<RotationInfo> f) {
        int[] res = new int[f.size()*5];
        for(int i = 0; i < f.size(); i++) {
            RotationInfo r = f.get(i);
            res[i*5] = r.x;
            res[i*5+1] = r.y;
            res[i*5+2] = r.z;
            res[i*5+3] = r.rotation;
            res[i*5+4] = r.isSide ? 1 : 0;
        }
        return res;
    }
}
