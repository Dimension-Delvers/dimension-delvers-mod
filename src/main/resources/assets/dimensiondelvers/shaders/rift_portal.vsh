#version 150

in vec3 Position;
in vec2 UV0;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out vec4 texProj0;
out vec4 texProj1;
out vec2 texCoord0;

vec4 getPortalProjection(vec3 position) {
    vec4 portalViewPos = ProjMat * vec4(position, 1.0);
    vec4 portalProjection = 0.5 * portalViewPos;
    portalProjection.xy = 0.5 * vec2(portalViewPos.x + portalViewPos.w, portalViewPos.y + portalViewPos.w);
    portalProjection.zw = portalViewPos.zw;
    return portalProjection;
}

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    texCoord0 = UV0;

    float aspect = ProjMat[1][1]/ProjMat[0][0];
    texProj0 = getPortalProjection(Position);
    texProj0.z = aspect;
    texProj1 = getPortalProjection(Position.zyx);
    texProj1.z = aspect;
}