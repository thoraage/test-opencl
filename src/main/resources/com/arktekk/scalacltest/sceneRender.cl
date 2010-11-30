struct vector {
  float x;
  float y;
  float z;
};

struct sphere {
  float x;
  float y;
  float z;
  float r;
};

struct spheres {
  struct sphere* ss;
  int c;
};

float sqr(float v) {
  return v * v;
}

float hit(struct vector pos, struct vector dir, struct sphere sphere) {
  float a = sqr(dir.x) + sqr(dir.y) + sqr(dir.z);
  float b = 2*dir.x*(pos.x - sphere.x) +  2*dir.y*(pos.y-sphere.y) +  2*dir.z*(pos.z-sphere.z);
  float c = sqr(sphere.x) + sqr(sphere.y) + sqr(sphere.z) + sqr(pos.x) + sqr(pos.y) + sqr(pos.z) +
                                    -2*(sphere.x*pos.x + sphere.y*pos.y + sphere.z*pos.z) - sqr(sphere.r);
  float p1 = sqr(b) - 4*a*c;
  if (p1 > 0) {
    float t1 = (-b - sqrt(p1)) / (2*a);
    float t2 = (-b + sqrt(p1)) / (2*a); 
    if (t1 > 0 && t2 > 0)
      return (t1 < t2) ? t1 : t2;
    else
      return -1.0f;
  } else
    return -1.0f;
}

float vectDot(struct vector v1, struct vector v2) {
  return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
}

struct vector normal(struct vector pos, struct vector dir, float t, struct sphere sphere) {
  struct vector hitPoint = { pos.x + t*dir.x, pos.y + t*dir.y, pos.z + t*dir.z };
  return (struct vector){hitPoint.x - sphere.x, hitPoint.y - sphere.y, hitPoint.z - sphere.z};
}

struct vector vectSub(struct vector v1, struct vector v2) {
  return (struct vector){v1.x - v2.x, v1.y - v2.y, v1.z - v2.z};
}

struct vector vectAdd(struct vector v1, struct vector v2) {
  return (struct vector){v1.x + v2.x, v1.y + v2.y, v1.z + v2.z};
}

struct vector vectConstMul(float c, struct vector v) {
  return (struct vector){v.x * c, v.y * c, v.z * c};
}

struct vector reflection(struct vector normal, struct vector incidence) {
  return vectSub(incidence, vectConstMul(2, vectConstMul(vectDot(incidence, normal), normal)));
}

float vectLength(struct vector v) {
  return sqrt(sqr(v.x) + sqr(v.y) + sqr(v.z));
}

struct vector vectNormalize(struct vector v) {
  float len = vectLength(v);
  return (struct vector) {v.x / len, v.y / len, v.z / len};
}

float fpow(float v, int p) {
  float ret = 1.0f;
  for (int i = 0; i < p; ++i)
    ret *= v;
  return ret;
}

float trace(struct spheres spheresStruct, struct vector origDir, struct vector lightDir, struct vector origPos) {
  float s = 0.0f;
  struct vector dir = origDir;
  struct vector pos = origPos;
  for (int round = 0; round < 10; ++round) {
    float t = -1.0f;
    int idx = -1;
    for (int i = 0; i < spheresStruct.c; ++i) {
      struct sphere sphere = spheresStruct.ss[i];
      float tmp = hit(pos, dir, sphere);
      if (tmp != -1.0f && (t == -1.0f || tmp < t)) {
        t = tmp;
        idx = i;
      }
    }
    if (t == -1.0f)
      return s;
    struct sphere sphere = spheresStruct.ss[idx];
    struct vector norm = vectNormalize(normal(pos, dir, t, sphere));
    struct vector reflect = reflection(norm, dir);
    float angle = acos(vectDot(lightDir, vectNormalize(reflect)));
    s += fpow(0.25f, round) * angle / 3.142f;
    pos = vectAdd(vectConstMul(t, dir), pos);
    dir = reflect;
  }
  return s;
}

int render(float x, float y, struct spheres spheresStruct, struct vector dir, struct vector lightDir) {
  struct vector pos = {x, y, 0.0f};
  float s = trace(spheresStruct, dir, lightDir, pos);
  int value = (int)((s > 1.0f ? 1.0f : s) * 255.0f);
  return value + (value << 8) + (value << 16);
}

__kernel void sceneRender(int width, int height, int workSize, __global int* output) {
  int workItem = get_global_id(0);
  struct sphere spheres[3] = {
    {0.5f, 0.0f, 5.0f, 0.25f},
    {-0.5f, 0.0f, 5.0f, 0.25f},
    {0.0f, 0.0f, 4.5f, 0.25f}
  };
  struct spheres spheresStruct = { spheres, 3 };
  struct vector dir = {0.0f, 0.0f, 1.0f};
  struct vector lightDir = vectNormalize((struct vector){1.0f, 1.0f, 1.0f});
  for (int i = 0; i < workSize; ++i) {
    int pos = i + workItem * workSize;
    float x = pos % width - width / 2;
    float y = pos / width - height / 2;
    output[pos] = render(2.0f * x / width, 2.0f * y / width, spheresStruct, dir, lightDir);
  }
}
