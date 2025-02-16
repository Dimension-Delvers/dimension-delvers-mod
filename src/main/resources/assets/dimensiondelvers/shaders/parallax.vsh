#version 150

in vec3 Position;
in vec2 UV0;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out vec4 texProj0;
out vec2 texCoord0;

vec4 projection_from_position(vec4 position) {
    vec4 projection = position * 0.5;
    projection.xy = vec2(projection.x + projection.w, projection.y + projection.w);
    projection.zw = position.zw;
    return projection;
}

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    float aspect = ProjMat[1][1]/ProjMat[0][0];
//    vec3 backPos = 100.0 * (Position + 0.5 * Normal);
    vec4 screenBackPos = ProjMat * vec4(Position, 1.0);
    texProj0 = projection_from_position(screenBackPos);
    texProj0.z = aspect;

    texCoord0 = UV0;
}