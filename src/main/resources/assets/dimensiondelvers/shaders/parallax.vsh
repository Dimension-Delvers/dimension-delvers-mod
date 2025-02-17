#version 150

in vec3 Position;
in vec2 UV0;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out vec4 texProj0;
out vec2 texCoord0;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    texCoord0 = UV0;

    float aspect = ProjMat[1][1]/ProjMat[0][0];
    vec4 portalViewPos = ProjMat * vec4(Position, 1.0);
    vec4 portalProjection = 0.5 * portalViewPos;
    portalProjection.xy = 0.5 * vec2(portalViewPos.x + portalViewPos.w, portalViewPos.y + portalViewPos.w);
    portalProjection.zw = portalViewPos.zw;
    texProj0 = portalProjection;
    texProj0.z = aspect;
}