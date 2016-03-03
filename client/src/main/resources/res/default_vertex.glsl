#version 150 core

in vec2 v_vertPos;
in vec2 v_texPos;
in vec3 v_color;

out vec2 f_texPos;
out vec3 f_color;

uniform mat4 u_model;
uniform mat4 u_view;
uniform mat4 u_projection;

void main() {
    f_texPos = v_texPos;
    f_color = v_color;
    mat4 mvp = u_model * u_view * u_projection;
    gl_Position = mvp * vec4(v_vertPos, 0.0, 1.0);
}
