#include <stdio.h>

const long long multiplier = 0x5DEECE66DLL;
const long long mask = (1LL << 48) - 1;

__device__
int random(long long seed) {
    seed = (seed ^ multiplier) & mask;
    return (int)((unsigned long long)(seed * 0xBB20B4600A69LL + 0x40942DE6BALL) >> 16);
}

__device__
long long getCoordRandom(int x, int y, int z) {
    long long l = (long long)(x * 3129871) ^ (long long)z * 116129781LL ^ (long long)y;
    l = l * l * 42317861LL + l * 11LL;
    return l >> 16;
}

__device__
int getTexture1131211(int x, int y, int z, int mod) {
    int rand = random(getCoordRandom(x, y, z));
    return abs(rand) % mod;
}

__device__
int getTexture(int x, int y, int z, int mod) {
    long long seed = getCoordRandom(x, y, z);
    seed = (seed ^ multiplier);
    seed = ((seed * multiplier) + 11LL) & mask;
    int next = (int)(seed >> (48 - 31));
    return (int) ((4LL * (long long)next) >> 31) % mod;
}

extern "C"
__global__
void find(int x_min, int x_max, int y_min, int y_max, int z_min, int z_max, int *rotations, int rotations_size, int version) {
    int x = x_min + blockIdx.x * blockDim.x + threadIdx.x;
    int y = y_min + blockIdx.y * blockDim.y;
    int z = z_min + blockIdx.z * blockDim.z + threadIdx.z;

    
    if(x > x_max || x < x_min || y > y_max || y < y_min || z > z_max || z < z_min) return;
    // if(x != 269115 || y != 64 || z != -453757) return;
    for(int j = 0; j < rotations_size; j++) {
        int rot_x = rotations[j*5];
        int rot_y = rotations[j*5+1];
        int rot_z = rotations[j*5+2];
        int rot_id = rotations[j*5+3];
        int rot_isside = rotations[j*5+4];
        if(rot_isside == 0) { // top/bottom face
            int actual = version == 1 ? getTexture(x + rot_x, y + rot_y, z + rot_z, 4) : getTexture1131211(x + rot_x, y + rot_y, z + rot_z, 4);
            // printf("X: %d Y: %d Z: %d rot_x: %d rot_y: %d rot_z: %d rot_id: %d actual: %d\n", x, y, z, rot_x, rot_y, rot_z, rot_id, actual);
            if(rot_id != actual) {
                return;
            }
        } else { // side face
            int actual = version == 1 ? getTexture(x + rot_x, y + rot_y, z + rot_z, 2) : getTexture1131211(x + rot_x, y + rot_y, z + rot_z, 2);
            if(rot_id != actual) {
                return;
            }
        }
    }
    printf("X: %d Y: %d Z: %d\n", x, y, z);
}
