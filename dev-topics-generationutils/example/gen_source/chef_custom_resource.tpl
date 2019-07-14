resource_name :chefkata 
property :kata, String, name_property: true

# Generation date: Run date: @@{.now}

action :create do
  execute 'ran' do
    command 'echo ran command > '@@{GEN_chef_file_target}'
    not_if { ::File.exist?('@@{GEN_chef_file_target}') }
  end

  git 'chefkata' do
    destination '@@{GEN_chef_repo_loc}'
    repository @@{GEN_chef_file_repo}
    action :nothing
    subscribes :sync, 'execute[ran]', :immediately
  end
end
