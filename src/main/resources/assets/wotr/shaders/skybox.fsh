#version 150

#moj_import <fog.glsl>

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec2 ScreenSize;
uniform vec4 FogColor;
uniform mat4 ProjMat;
uniform mat4 ModelViewMatrix;
uniform sampler2D Sampler0;
uniform vec2 View;

in float vertexDistance;

out vec4 fragColor;

vec2 getUV(vec3 t3) {
    vec2 t2;
    t3=normalize(t3)/sqrt(2.0);
    vec3 q3=abs(t3);
    if ((q3.x>=q3.y)&&(q3.x>=q3.z)) {
        t2.x=0.5-t3.z/t3.x;
        t2.y=0.5-t3.y/q3.x;
    }
    else if ((q3.y>=q3.x)&&(q3.y>=q3.z)) {
        t2.x=0.5+t3.x/q3.y;
        t2.y=0.5+t3.z/t3.y;
    }
    else {
        t2.x=0.5+t3.x/t3.z;
        t2.y=0.5-t3.y/q3.z;
    }
    return t2;
}

vec2 getUVOffset(vec3 dir1) {
    dir1=normalize(dir1)/sqrt(2.0);
    vec3 dir=abs(dir1);
    // EAST
    if ((dir.x>=dir.y)&&(dir.x>=dir.z)&&(dir1.x > 0.0)) {
        return vec2(2.0, 1.0);
    }
    // WEST
    if ((dir.x>=dir.y)&&(dir.x>=dir.z)&&(dir1.x < 0.0)) {
        return vec2(0.0, 1.0);
    }
    // UP
    if ((dir.y>=dir.x)&&(dir.y>=dir.z)&&(dir1.y > 0.0)) {
        return vec2(1.0, 0.0);
    }
    // DOWN
    if ((dir.y>=dir.x)&&(dir.y>=dir.z)&&(dir1.y < 0.0)) {
        return vec2(1.0, 2.0);
    }
    // SOUTH
    if (dir1.z > 0.0) {
        return vec2(1.0, 1.0);
    }
    // NORTH
    return vec2(3.0, 1.0);
}

const float PI = 3.14159265359;

mat2 rotate2d(float theta) {
    float s = sin(theta), c = cos(theta);
    return mat2(c, -s, s, c);
}

mat3 camera(vec3 cameraPos, vec3 lookAtPoint) {
    vec3 cd = normalize(lookAtPoint - cameraPos);
    vec3 cr = normalize(cross(vec3(0, 1, 0), cd));
    vec3 cu = normalize(cross(cd, cr));

    return mat3(-cr, cu, -cd);
}

void main() {
    vec2 uv = (gl_FragCoord.xy - 0.5 * ScreenSize.xy) / ScreenSize.yy;

    vec3 lp = vec3(0);
    vec3 ro = vec3(0, 0, 10);
    ro.yz *= rotate2d(mix(-PI/2., PI/2., 0.5 - View.x / 180.1));
    ro.xz *= rotate2d(mix(-PI, PI, View.y / 360.0));

    vec3 rd = camera(ro, lp) * normalize(vec3(uv, -0.5));
    //vec4 a = test(rd);
    //fragColor = mix(vec4(rd / 2.0 + vec3(0.5), 1.0), a, a.a);
    fragColor = texture(Sampler0, (getUV(rd)/2.0+0.25+getUVOffset(rd))/4.0);
}

/*
uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec2 ScreenSize;
uniform vec4 FogColor;
uniform mat4 ModelViewMatrix;
uniform sampler2D Sampler0;

in float vertexDistance;

out vec4 fragColor;

void main() {
    vec2 uv = 2.5 * (gl_FragCoord.xy - 0.5 * ScreenSize.xy) / ScreenSize.yy;

    fragColor = texture(Sampler0, uv);
}
*/